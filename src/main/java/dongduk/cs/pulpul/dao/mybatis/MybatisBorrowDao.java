package dongduk.cs.pulpul.dao.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dongduk.cs.pulpul.dao.BorrowDao;
import dongduk.cs.pulpul.dao.mybatis.mapper.BorrowMapper;
import dongduk.cs.pulpul.domain.Alert;
import dongduk.cs.pulpul.domain.Borrow;

@Repository
public class MybatisBorrowDao implements BorrowDao {

	@Autowired
	private BorrowMapper borrowMapper;
	
	@Override
	public List<Borrow> findBorrowReservationByMember(String memberId) {
		return borrowMapper.selectBorrowReservationByMemberId(memberId);
	}
	
	@Override
	public List<Borrow> findBorrowReservationByItem(String itemId) {
		return borrowMapper.selectBorrowReservationByItemId(itemId);
	}
	
	@Override
	public int findBorrowReservationCount(Borrow borrow) {
		return borrowMapper.selectBorrowReservationNumber(borrow);
	}

	@Override
	public boolean createBorrowReservation(Borrow borrow) {
		int ck = borrowMapper.insertBorrowReservation(borrow);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public boolean deleteBorrowReservation(Borrow borrow) {
		int ck = borrowMapper.deleteBorrowReservation(borrow);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public int checkNumberBorrowReservation(String itemId) {
		return borrowMapper.selectReservationNumber(itemId);
	}

	@Override
	public boolean changeIsFirstBooker(Borrow borrow) {
		int ck = borrowMapper.updateIsFirstBooker(borrow);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public List<Alert> findAlertByAlertDate() {
		return borrowMapper.selectAlertByAlertDate();
	}

	@Override
	public List<Alert> findAlertByMember(String memberId) {
		return borrowMapper.selectAlertByMemberId(memberId);
	}

	@Override
	public int findAlertCountByIsRead(String memberId) {
		return borrowMapper.selectAlertCountByIsRead(memberId);
	}

	@Override
	public boolean createAlert(Alert alert) {
		int ck = borrowMapper.insertAlert(alert);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public boolean deleteAlert(Alert alert) {
		int ck = borrowMapper.deleteAlert(alert);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public boolean changeIsRead(Alert alert) {
		int ck = borrowMapper.updateIsRead(alert);
		if (ck == 0) return false;
		return true;
	}

	@Override
	public List<Borrow> findBorrowByMember(String memberId, String identity) {
		return borrowMapper.selectBorrowByMemberId(memberId, identity);
	}

	@Override
	public List<Borrow> findBorrowByItem(String itemId) {
		return borrowMapper.selectBorrowByItemId(itemId);
	}

	@Override
	public boolean createBorrow(Borrow borrow) {
		int ck = borrowMapper.insertBorrow(borrow);
		if (ck < 0) return false;
		return true;
	}
	
	@Override
	public boolean changeBorrowStatus(Borrow borrow) {
		int ck = borrowMapper.updateBorrowStatus(borrow);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public boolean changeReturnDate(Borrow borrow) {
		int ck = borrowMapper.updateReturnDate(borrow);
		if (ck < 0) return false;
		return true;
	}

	@Override
	public Borrow findBorrowById(int borrowId) {
		return borrowMapper.selectBorrowById(borrowId);
	}

	@Override
	public Borrow findCurrBorrowByItem(String itemId) {
		return borrowMapper.selectCurrBorrowByItem(itemId);
	}

	@Override
	public List<Alert> findAllAlert() {
		return borrowMapper.selectAllAlert();
	}

	@Override
	public Borrow findFirstBookersBorrowReservation(Borrow borrow) {
		return borrowMapper.selectFirstBookersBorrowReservation(borrow);
	}

	@Override
	public Borrow findBorrowReservationByAlert(Alert a) {
		return borrowMapper.selectBorrowReservationByAlert(a);
	}
}
