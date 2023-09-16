package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
public class Review implements Serializable {

	private int id; /*후기 식별 번호*/
	private String writeDate;
	private String content;
	private int rating; 
	private Order order; /*주문 정보*/
	private Item item; /*주문 품목*/
	private String imageUrl; /*후기 이미지 경로*/
}
