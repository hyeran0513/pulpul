package dongduk.cs.pulpul.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dongduk.cs.pulpul.dao.BorrowDao;
import dongduk.cs.pulpul.dao.ItemDao;
import dongduk.cs.pulpul.domain.Alert;
import dongduk.cs.pulpul.domain.Borrow;
import dongduk.cs.pulpul.domain.ShareThing;

@Service
public class BorrowServiceImpl implements BorrowService {
	private final BorrowDao borrowDao;
	private final ItemDao itemDao;

	@Autowired
	public BorrowServiceImpl(BorrowDao borrowDao, ItemDao itemDao) {
		this.borrowDao = borrowDao;
		this.itemDao = itemDao;
	}
	
	@Autowired		// SchedulerConfig에 설정된 TaskScheduler 빈을 주입 받음
	private TaskScheduler scheduler;
	
	@Transactional
	public void reservationCancelScheduler(Date closingTime) {
		Runnable updateTableRunner = new Runnable() { // anonymous class 정의
			@Override
			public void run() {   // 스케쥴러에 의해 미래의 특정 시점에 실행될 작업을 정의			
				Date date = new Date(); // 현재 시각
				// 전체 알림을 가져와서(알림에는 대출 가능 알림만 존재), date + 3, 해당 일자를 지난 값을 갖는 event의 상태를 변경 
				List<Alert> alertList = borrowDao.findAlertByAlertDate();
				for (Alert a : alertList) {
					System.out.println(a.toString());
					// 예약 취소
					Borrow borrow = getBorrowReservationByAlert(a);
					if (borrow != null) {
						cancelBorrowReservation(borrow);
						
						// 다음 예약자가 있다면 is_first_booker를 1로 만든 뒤 알림 생성
						if (checkNumberBorrowReservation(borrow.getShareThing().getItem().getId()) != 0) {
							changeIsFirstBooker(borrow);
							
							Alert alert = new Alert();
							Borrow firstReservation = borrowDao.findFirstBookersBorrowReservation(borrow);
							alert.setMemberId(firstReservation.getBorrower().getId());
							alert.setShareThingId(borrow.getShareThing().getItem().getId());
							borrowDao.createAlert(alert);
						}
						else { // 다음 예약자가 없다면 shareThing is_borrowed를 0으로
							ShareThing shareThing = borrow.getShareThing();
							shareThing.setIsBorrowed(0);
							changeIsBorrowed(shareThing);
						}
					}
				}

				System.out.println("updateTableRunner is executed at " + date);
			}
		};

		// 스케줄 생성: closingTime에 updateTableRunner.run() 메소드 실행
		Calendar cal =  Calendar.getInstance();
		cal.setTime(closingTime);
		cal.add(Calendar.DATE, 3);
		cal.add(Calendar.SECOND, 1);
		closingTime = cal.getTime(); 
		scheduler.schedule(updateTableRunner, closingTime);		
		System.out.println("updateTableRunner has been scheduled to execute at " + closingTime);
	}
	
	// 알림 삭제 - 첫번째 대기자인데 예약 취소한 경우, 대여신청을 한 경우, 예약취소를 한 경우
	public boolean removeAlert(Alert alert) {
		return borrowDao.deleteAlert(alert);
	}
	
	// 알림 목록 조회
	public List<Alert> getAlertByMember(String memberId) {
		return borrowDao.findAlertByMember(memberId);
	}
	
	// 알림 읽음 변경
	public boolean changeIsRead(Alert alert) {
		return borrowDao.changeIsRead(alert);
	}
	
	// 회원의 읽지 않은 알림 수
	public int getAlertCountByIsRead(String memberId) {
		return borrowDao.findAlertCountByIsRead(memberId);
	}

	// 알림 생성 - is_first_booker가 1인 경우
	boolean addAlert(Alert alert) {
		return borrowDao.createAlert(alert);
	}
	
	// 전체 알림 조회
	@Override
	public List<Alert> getAllAlert() {
		return borrowDao.findAllAlert();
	}
	
	// 회원 아이디로 예약 목록 조회
	public List<Borrow> getBorrowReservationByMember(String memberId) {
		return borrowDao.findBorrowReservationByMember(memberId);
	}
	
	// 공유물품 id로 예약 목록 조회
	public List<Borrow> getBorrowReservationByItem(String itemId) {
		return borrowDao.findBorrowReservationByItem(itemId);
	}
	
	// 알림으로 예약 목록 조회
	public Borrow getBorrowReservationByAlert(Alert alert) {
		return borrowDao.findBorrowReservationByAlert(alert);
	}
	
	// 회원 id와 공유물품 id로 예약 목록 조회
	@Override
	public int getBorrowReservationCount(Borrow borrow) {
		return borrowDao.findBorrowReservationCount(borrow);
	}
	
	// 첫 번째 예약자의 예약 정보 조회
	public Borrow getFirstBookersBorrowReservation(Borrow borrow) {
		return borrowDao.findFirstBookersBorrowReservation(borrow);
	}
	
	// 예약 생성
	@Transactional
	public boolean makeBorrowReservation(Borrow borrow) {
		int borrowReservationNum = borrowDao.checkNumberBorrowReservation(borrow.getShareThing().getItem().getId());
		if (borrowReservationNum == 0) {
			borrow.setIsFirstBooker(1);
		}
		else if (borrowReservationNum == 1){
			borrow.setIsFirstBooker(0);
		}
		else {
			return false;
		}
		return borrowDao.createBorrowReservation(borrow);
	}
	
	// 예약 취소 - 예약 취소 또는 대여 시작할 때 예약 삭제
	@Transactional
	public boolean cancelBorrowReservation(Borrow borrow) {
		// 대여 예약 알림 삭제
		Alert alert = new Alert();
		alert.setShareThingId(borrow.getShareThing().getItem().getId());
		alert.setMemberId(borrow.getBorrower().getId());
		removeAlert(alert);
		return borrowDao.deleteBorrowReservation(borrow);
	}
	
	// 회원 아이디로 대여 목록 조회
	public List<Borrow> getBorrowByMember(String memberId, String identity) {
		return borrowDao.findBorrowByMember(memberId, identity);
	}
	
	// 품목 아이디로 대여현황 조회
	public List<Borrow> getBorrowByItem(String itemId) {
		return borrowDao.findBorrowByItem(itemId);
	}
	
	// 현재 대여 중인 품목 대여 현황 조회
	public Borrow getCurrBorrowByItem(String itemId) {
		return borrowDao.findCurrBorrowByItem(itemId);
	}

	// 대여 생성
	@Transactional
	public boolean borrow(Borrow borrow) {
		// 반납일 계산
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar borrowCal = Calendar.getInstance();
		String borrowDate = transFormat.format(borrowCal.getTime());
		Calendar returnCal = Calendar.getInstance();
		returnCal.add(Calendar.DATE, Integer.parseInt(borrow.getDate()));
		String returnDate = transFormat.format(returnCal.getTime());

		borrow.setBorrowDate(borrowDate);
		borrow.setReturnDate(returnDate);
		
		// 공유물품 대여 상태 변경
		ShareThing tempShareThing = borrow.getShareThing();
		tempShareThing.setIsBorrowed(1);
		borrow.setShareThing(tempShareThing);
		
		// 대여
		boolean success = borrowDao.createBorrow(borrow);
		if (success) {
			// 예약자가 대여 시 isFirstBooker 수정
			if (borrow.getIsFirstBooker() == 1) {
				cancelBorrowReservation(borrow);
				if (checkNumberBorrowReservation(borrow.getShareThing().getItem().getId()) != 0) {
					changeIsFirstBooker(borrow);
				}
			}
			
			// 대여 예약 내역 삭제
			cancelBorrowReservation(borrow);
			
			// 대여 예약 알림 삭제
			Alert alert = new Alert();
			alert.setShareThingId(borrow.getShareThing().getItem().getId());
			alert.setMemberId(borrow.getBorrower().getId());
			removeAlert(alert);
			
			return itemDao.changeIsBorrowed(borrow.getShareThing());
		}
		return false;
	}

	// 공유 물품 대여 여부 변경 - 사용자가 반납 버튼 눌렀을 때 예약자 수 확인
	// 예약자가 없으면 대여 가능 상태(0)로, 대여하기 클릭 시 대여 중 상태(1)로 변경
	// is_borrowed = 1:대여 불가 0: 대여 가능
	@Transactional
	boolean changeIsBorrowed(ShareThing shareThing) {
		int reservationNumber = borrowDao.checkNumberBorrowReservation(shareThing.getItem().getId());
		if (reservationNumber == 0) {
			shareThing.setIsBorrowed(0);
		}
		else {
			shareThing.setIsBorrowed(1);
		}
		return itemDao.changeIsBorrowed(shareThing);
	}
	
	// 연장 - borrow_status가 대여 상태면, 1회 연장 가능
	// is_extended = 1로 변경하고, 연장 기간 계산해서 returnDate 변경
	@Transactional
	public boolean extendBorrow(Borrow borrow) {
		borrow.setIsExtended(1);
		
		String returnDate = borrow.getReturnDate();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = transFormat.parse(returnDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); // 시간 설정
		cal.add(Calendar.DATE, 14); // 일 연산
		
		SimpleDateFormat transFormat2 = new SimpleDateFormat("yyyyMMdd");
		returnDate = transFormat2.format(cal.getTime());
		
		borrow.setReturnDate(returnDate);
		
		return borrowDao.changeReturnDate(borrow);
	}

	// 공유 물품 대여 상태 변경 - 반납이 되면 대여한 물품 대여 상태 변경
	// borrow_status = 1:대여 중 0:반납
	@Transactional
	public boolean returnShareThing(Borrow borrow) {
		// 예약자가 없는 경우에는 공유 물품 대여 가능 상태 변경
		if (checkNumberBorrowReservation(borrow.getShareThing().getItem().getId()) == 0) {
			ShareThing shareThing = borrow.getShareThing();
			shareThing.setIsBorrowed(0);
			itemDao.changeIsBorrowed(shareThing);
		}
		else {
			// 첫 번째 예약자에게 알림 생성
			Alert alert = new Alert();
			Borrow firstReservation = borrowDao.findFirstBookersBorrowReservation(borrow);
			alert.setMemberId(firstReservation.getBorrower().getId());
			alert.setShareThingId(borrow.getShareThing().getItem().getId());
			borrowDao.createAlert(alert);
		}
		borrow.setBorrowStatus("0");
		return borrowDao.changeBorrowStatus(borrow);
	}

	// 공유물품 대기자 수 확인 -
	// 대기자가 없거나 한 명만 있으면 예약 가능 -> 예약 시 확인
	int checkNumberBorrowReservation(String itemId) {
		return borrowDao.checkNumberBorrowReservation(itemId);
	}
	
	// is_first_booker 상태 변경
	// 두번째->첫번째 예약자가 된 경우
	boolean changeIsFirstBooker(Borrow borrow) {
		borrow.setIsFirstBooker(1);
		return borrowDao.changeIsFirstBooker(borrow);
	}

	// 대여 id로 대여 조회
	@Override
	public Borrow getBorrowById(int borrowId) {
		return borrowDao.findBorrowById(borrowId);
	}
}
