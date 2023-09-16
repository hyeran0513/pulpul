package dongduk.cs.pulpul.controller;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@SuppressWarnings("serial")
public class FileCommand implements Serializable {
	
	String path;
	MultipartFile file;
	MultipartFile[] files;
	
}
