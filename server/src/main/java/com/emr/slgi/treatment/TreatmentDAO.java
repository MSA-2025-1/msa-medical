package com.emr.slgi.treatment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.page.TreatmentHitoryRequestDTO;

@Mapper
public interface TreatmentDAO {

	public int updateTreatment(Treatment treatment);
	
	public Treatment selectTreatment(int id);

	public int getHistoryCount(TreatmentHitoryRequestDTO treatmentHitoryRequestDTO);

	public List<Treatment> getHistory(TreatmentHitoryRequestDTO treatmentHitoryRequestDTO);

	public void insertTreatment(String uuid);

	public DocumentDTO getDocument(int treatmentId);


}
