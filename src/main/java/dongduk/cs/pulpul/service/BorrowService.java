package dongduk.cs.pulpul.service;

import java.util.Date;
import java.util.List;

import dongduk.cs.pulpul.domain.Alert;
import dongduk.cs.pulpul.domain.Borrow;

public interface BorrowService {
	/*
	 * Scheduler
	 */
	// 예약 자동 취소
	public void reservationCancelScheduler(Date closingTime);
	
	/*
	 * 대여 - 알림 관리
	 */
	// 전체 알림 목록 조회
	List<Alert> getAllAlert();
	
	// 회원 id로 알림 목록 조회
	List<Alert> getAlertByMember(String memberId);
	
	// 회원의 읽지 않은 알림 개수
	int getAlertCountByIsRead(String memberId);
	
	// 알림 읽음 처리
	boolean changeIsRead(Alert alert);
	
	/*
	 * 대여 - 예약 관리
	 */
	// 회원 id로 대여 예약 목록 조회
	List<Borrow> getBorrowReservationByMember(String memberId);
	
	// 공유물품 id로 예약 목록 조회
	List<Borrow> getBorrowReservationByItem(String itemId);
	
	// 회원 id와 공유물품 id로 예약 개수 조회
	public int getBorrowReservationCount(Borrow borrow);
	
	// 대여 예약 생성
	boolean makeBorrowReservation(Borrow borrow);
	
	// 대여 예약 취소
	boolean cancelBorrowReservation(Borrow borrow);
	
	/*
	 * 대여 - 대여 관리
	 */
	// 회원 id로 대여 목록 조회
	List<Borrow> getBorrowByMember(String memberId, String identity);
	
	// 품목 id로 대여 목록 조회
	List<Borrow> getBorrowByItem(String itemId);
	
	// 특정 공유 물품의 현재 대여 조회
	public Borrow getCurrBorrowByItem(String itemId);
	
	// 대여 id로 대여 조회
	Borrow getBorrowById(int borrowId);
	
	// 대여
	boolean borrow(Borrow borrow);
	
	// 대여 연장
	boolean extendBorrow(Borrow borrow);
	
	// 반납 (returnItem에서 메소드명 변경)
	boolean returnShareThing(Borrow borrow);
}
