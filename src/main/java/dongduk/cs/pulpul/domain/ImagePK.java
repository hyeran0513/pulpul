package dongduk.cs.pulpul.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@SuppressWarnings("serial")
public class ImagePK implements Serializable {
	
	private String memberId;
	private String categoryId;
	private String imageSrc;
	
}
