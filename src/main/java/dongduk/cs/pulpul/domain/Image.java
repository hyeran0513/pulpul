package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@Entity
@IdClass(ImagePK.class)
public class Image implements Serializable {
	@Id 
	@Column(name="member_id")
	private String memberId;

	@Id
	private String categoryId;
	
	@Id
	@Column(name="image_src")
	private String imageSrc;
}
