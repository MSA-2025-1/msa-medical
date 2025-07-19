package com.emr.slgi.attachment;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService {
	private final AttachmentDAO attachmentDAO;
	@Value("${file.upload-dir}")
	private String uploadDir;
	

	public void insertAttachment(MultipartFile[] files, int treatmentId) {
		for(MultipartFile file : files) {
			try {
				String originalName = file.getOriginalFilename();
                String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
                String contentType = file.getContentType();
                long size = file.getSize();
                String fileName = UUID.randomUUID() + "." + extension;
                String path = uploadDir+ "/" + fileName;
                file.transferTo(new File(path));
                
                Attachment att = new Attachment();
                att.setTreatmentId(treatmentId);
                att.setOriginalName(originalName);
                att.setFileName(fileName);
                att.setPath(path);
                att.setSize((int) size);
                att.setContentType(contentType);
                att.setExtension(extension);

                attachmentDAO.insertAttachment(att);
			}catch (Exception e) {
				throw new RuntimeException("파일 업로드 실패",e);
			}
		}
		
	}

}
