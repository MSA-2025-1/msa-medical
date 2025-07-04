package com.emr.slgi.treatment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.page.TreatmentHitoryRequestDTO;

@Mapper
public interface TreatmentDAO {

	public int insertTreatment(Treatment treatment);
	public int updateTreatment(Treatment treatment);
	
	public Treatment selectTreatment(int id);

	public int getHistoryCount(TreatmentHitoryRequestDTO treatmentHitoryRequestDTO);

	public List<Treatment> getHistory(TreatmentHitoryRequestDTO treatmentHitoryRequestDTO);

	public Treatment selectedPatientUuid(String doctorUuid);
	public void insertTreatment(String patientUuid, String doctorUuid);


}
