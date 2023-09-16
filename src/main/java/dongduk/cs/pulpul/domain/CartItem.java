package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CartItem implements Serializable {
	
	private String goodsId;
	private Goods goods; /*상품 정보*/
	@Min(1)
	private int quantity; /*담은 수량*/
	private int price; /*상품 가격(단가x수량)*/

}
