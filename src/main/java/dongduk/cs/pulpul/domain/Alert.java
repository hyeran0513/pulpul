package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter
@ToString
public class Alert implements Serializable {

	private String memberId; /*회원 아이디*/ 
	private String shareThingId; /*예약한 공유물품 식별번호*/
	private String alertDate; /*알림일*/
	private String content; /*알림 내용*/
	private int isRead; /*알림 읽음 여부*/
	private String pageUrl; /*공유물품 상세정보 페이지 경로*/
	
}
