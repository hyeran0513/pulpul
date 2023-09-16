package dongduk.cs.pulpul.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Cart implements Serializable {

	private String memberId; /*회원 아이디*/
	private List<CartItem> cartItemList; /*담긴 상품*/

}
