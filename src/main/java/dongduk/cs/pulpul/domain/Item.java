package dongduk.cs.pulpul.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@SuppressWarnings("serial")
public class Item implements Serializable {

	private String id; /*품목 식별번호*/
	@NotBlank
	private String name; /*품목 이름*/
	private String uploadDate; /*품목 생성일*/
	@NotBlank
	private String description; /*품목 설명*/
	private int openStatus; /*품목 공개여부*/
	private Market market; /*마켓 정보*/
	private String thumbnailUrl; /*품목 대표 이미지 경로*/
	private List<String> imageUrlList; /*품목 이미지 경로 리스트*/
	
}
