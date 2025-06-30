package com.emr.slgi.treatment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.emr.slgi.attachment.Attachment;
import com.emr.slgi.attachment.AttachmentDAO;
import com.emr.slgi.attachment.AttachmentService;
import com.emr.slgi.diagnosis.Diagnosis;
import com.emr.slgi.diagnosis.DiagnosisDAO;
import com.emr.slgi.page.PageDTO;
import com.emr.slgi.page.TreatmentHitoryRequestDTO;
import com.emr.slgi.page.PageResponseDTO;
import com.emr.slgi.prescription.Prescription;
import com.emr.slgi.prescription.PrescriptionDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TreatmentService {
	private final TreatmentDAO treatmentDAO;
	private final AttachmentService attachmentService;
	private final PrescriptionDAO prescriptionDAO;
	private final DiagnosisDAO diagnosisDAO;
	private final AttachmentDAO attachmentDAO;
	
	public int insertTreatment(Treatment treatment) {
		treatmentDAO.insertTreatment(treatment);
		return treatment.getId();
	}
	
	@Transactional
	public void totalInsert(TotalTreatmentDTO data, MultipartFile[] files) {
		
		Treatment treatment = data.getTreatment();
		treatmentDAO.insertTreatment(treatment);
		Integer treatmentId = treatment.getId();
		
		if(files != null && files.length>0) {
			attachmentService.insertAttachment(files, treatmentId);
		}
		
		if (data.getPrescriptions() != null) {
			for (Prescription p : data.getPrescriptions()) {
		        p.setTreatmentId(treatmentId);
		        prescriptionDAO.insertPrescription(p);
		    }
		}
		
		if (data.getDiagnosis() != null) {
			for (Diagnosis d : data.getDiagnosis()) {
		        d.setTreatmentId(treatmentId);
		        diagnosisDAO.insertDiagnosis(d);
		    }
			
		}
	}

	public PageResponseDTO<Treatment> getHistory(TreatmentHitoryRequestDTO treatmentHitoryRequestDTO) {
		int totalCount = treatmentDAO.getHistoryCount(treatmentHitoryRequestDTO);
		List<Treatment> list = treatmentDAO.getHistory(treatmentHitoryRequestDTO);
		PageDTO pageDTO = new PageDTO(totalCount, treatmentHitoryRequestDTO.getPageNo(),treatmentHitoryRequestDTO.getSize());
		return new PageResponseDTO<Treatment>(pageDTO, list);
	}
	
	public TotalTreatmentDTO getTotalDetail(int id) {
		Treatment treatment = treatmentDAO.selectTreatment(id);
	    List<Prescription> prescriptions = prescriptionDAO.selectPrescriptions(id);
	    List<Diagnosis> diagnosis = diagnosisDAO.selectDiagnosis(id);
	    List<Attachment> attachments = attachmentDAO.selectAttachments(id);

	    TotalTreatmentDTO total = new TotalTreatmentDTO();
	    total.setTreatment(treatment);
	    total.setPrescriptions(prescriptions);
	    total.setDiagnosis(diagnosis);
	    total.setAttachments(attachments); 
		
		return total;
	}

}
