package dongduk.cs.pulpul.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class Order implements Serializable {

	private int id; /*주문 식별번호*/
	private String orderDate; /*주문일*/
	private int totalPrice; /*총 지불 금액*/
	private int usedPoint; /*사용 포인트*/
	@NotBlank
	private String cardCompany; /*카드사*/
	@Pattern(regexp="\\d{4}-\\d{4}-\\d{4}-\\d{4}")
	private String cardNumber; /*카드 번호*/
	@Pattern(regexp="\\d{2}/\\d{2}")
	private String expiryDate; /*카드 만료일*/
	private String trackingNumber; /*운송장 번호*/
	private int orderStatus; /*주문 상태*/
	private Member seller; /*판매자 정보*/
	private Member buyer; /*구매자 정보*/
	@NotEmpty
	private List<CartItem> goodsList; /*주문상품정보 리스트*/
	
}
