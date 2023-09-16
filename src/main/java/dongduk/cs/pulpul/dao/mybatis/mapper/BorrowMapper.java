package dongduk.cs.pulpul.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import dongduk.cs.pulpul.domain.Alert;
import dongduk.cs.pulpul.domain.Borrow;

@Mapper
public interface BorrowMapper {

	List<Alert> selectAlertByAlertDate();
	
	List<Alert> selectAlertByMemberId(String memberId);
	
	int selectAlertCountByIsRead(String memberId); 
	
	int insertAlert(Alert alert);
	
	int deleteAlert(Alert alert);
	
	int updateIsRead(Alert alert); 

	List<Alert> selectAllAlert();
	
	List<Borrow> selectBorrowReservationByMemberId(String memberId);
	
	List<Borrow> selectBorrowReservationByItemId(String itemId);
	
	int insertBorrowReservation(Borrow borrow);
	
	int deleteBorrowReservation(Borrow borrow);
	
	int selectReservationNumber(String itemId);
	
	Borrow selectFirstBookersBorrowReservation(Borrow borrow);

	Borrow selectBorrowReservationByAlert(Alert a);
	
	int updateIsFirstBooker(Borrow borrow);
	
	List<Borrow> selectBorrowByMemberId(String memberId, String identity);
	
	List<Borrow> selectBorrowByItemId(String itemId);
	
	int insertBorrow(Borrow borrow);
	
	int updateBorrowStatus(Borrow borrow);
	
	int updateReturnDate(Borrow borrow);
	
	Borrow selectBorrowById(int borrowId);

	Borrow selectCurrBorrowByItem(String itemId);

	int selectBorrowReservationNumber(Borrow borrow);
}
