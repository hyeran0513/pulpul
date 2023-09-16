package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@Entity
public class Market implements Serializable {

	@Id
	@SequenceGenerator(name="market_seq",
		sequenceName="MARKET_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
		generator="market_seq")
	@Column(name="market_id")
	private int id; /*마켓 식별 번호*/
	
	@Column(name="member_id")
	private String memberId;
	
	@NotBlank
	private String name; /*마켓 이름*/
	@NotBlank
	private String intro; /*마켓 소개*/
	
	@NotBlank
	@Column(name = "contactable_time")
	private String contactableTime; /*연락가능 시간*/
	private String policy; /*교환/반품/환불 정책*/
	private String precaution; /*구매 전 유의사항*/
	
	@Column(name="open_status")
	private String openStatus; /*마켓 공개여부*/
	
	@OneToOne
	@JoinColumn(name="member_id", insertable=false, updatable=false)
	private Member member; /*회원정보*/
	
	@Transient
	private String imageUrl;
}
