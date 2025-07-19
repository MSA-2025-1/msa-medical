package com.emr.slgi.attachment;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class AttachmentController {
	
	@Value("${file.upload-dir}")
	private String uploadDir ; 
	
	@GetMapping("/{fileName}")
	public ResponseEntity<Object> getFile(@PathVariable("fileName") String fileName) throws IOException {
		Path path = Paths.get(uploadDir).resolve(fileName);
		// 경로 탈출 방지
	    if (!path.startsWith(Paths.get(uploadDir))) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.");
	    }

	    Resource resource = new UrlResource(path.toUri());
	    if (!resource.exists()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
	    }
		
		if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }
		return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
	}
}
