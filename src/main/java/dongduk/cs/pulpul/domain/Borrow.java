package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
public class Borrow implements Serializable {

	private int id; /*대여 식별 번호*/
	private ShareThing shareThing; /*물품 정보*/
	private String borrowDate; /*대여일*/
	private String returnDate; /*반납일*/
	private String date; /*대여일수*/
	private String borrowStatus; /*대여 상태*/
	private int isExtended; /*대여기한 연장 여부*/
	private Member lender; /*대여해 준 회원 정보*/
	private Member borrower; /*대여받은 회원정보*/
	private int isFirstBooker; /*첫번째 예약자 여부*/
	
}
