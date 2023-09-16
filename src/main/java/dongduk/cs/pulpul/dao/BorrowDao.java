package dongduk.cs.pulpul.dao;

import java.util.List;

import dongduk.cs.pulpul.domain.Alert;
import dongduk.cs.pulpul.domain.Borrow;

public interface BorrowDao {
	
	/*
	 * 대여 - 알림 관리
	 */
	// 알림일로부터 3일이 지난 알림 목록 조회
	List<Alert> findAlertByAlertDate();
	
	// 회원 id로 알림 목록 조회
	List<Alert> findAlertByMember(String memberId);
	
	// 읽지 않은 알림 개수 조회 - 추가
	int findAlertCountByIsRead(String memberId);
	
	// 알림 생성
	boolean createAlert(Alert alert);
	
	// 알림 삭제
	boolean deleteAlert(Alert alert);
	
	// 알림 읽음여부 변경 - 추가
	boolean changeIsRead(Alert alert);
	
	// 전체 알림 조회
	List<Alert> findAllAlert();
	
	/*
	 * 대여 - 예약 관리
	 */
	// 회원 id로 대여 예약 조회
	List<Borrow> findBorrowReservationByMember(String memberId);
	
	// 공유물품 id로 대여 예약 조회
	List<Borrow> findBorrowReservationByItem(String itemId);
	
	// 알림으로 대여 예약 조회
	Borrow findBorrowReservationByAlert(Alert a);
	
	// 회원 id와 공유물품 id로 예약 개수 조회
	int findBorrowReservationCount(Borrow borrow);
	
	// 대여 예약 생성
	boolean createBorrowReservation(Borrow borrow);
	
	// 대여 예약 삭제
	boolean deleteBorrowReservation(Borrow borrow);
	
	// 대여 예약 수 확인
	int checkNumberBorrowReservation(String itemId);
	
	// 두번째 예약자를 첫번째 예약자로 변경
	boolean changeIsFirstBooker(Borrow borrow);
	
	// 첫 번째 예약자의 예약 정보 확인
	Borrow findFirstBookersBorrowReservation(Borrow borrow);
	
	/*
	 * 대여 - 대여 관리
	 */
	// 회원 id로 대여 목록 조회
	List<Borrow> findBorrowByMember(String memberId, String identity);
	
	// 품목 id로 대여 목록 조회
	List<Borrow> findBorrowByItem(String itemId);
	
	// 대여 id로 대여 조회
	Borrow findBorrowById(int borrowId);
	
	// 대여 생성
	boolean createBorrow(Borrow borrow);
	
	// 대여 상태 변경
	boolean changeBorrowStatus(Borrow borrow);
	
	// 대여 반납일, 연장 여부 변경
	boolean changeReturnDate(Borrow borrow);

	// 공유물품 id의 현재 대여 현황 조회
	Borrow findCurrBorrowByItem(String itemId);
	
}
