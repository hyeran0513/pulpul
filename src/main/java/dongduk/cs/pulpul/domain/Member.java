package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

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
public class Member implements Serializable {

	@Id
	@Column(name="member_id")
	private String id;
	private String password;
	@Transient
	private String passwordCheck;
	private String name;
	private String birth;
	private String address;
	
	@Column(name="address_detail")
	private String addressDetail;
	private String zip;
	private String phone;
	private String email;
	private int point;
}
