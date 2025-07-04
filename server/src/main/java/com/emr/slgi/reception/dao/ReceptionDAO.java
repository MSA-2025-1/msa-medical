package com.emr.slgi.reception.dao;

import com.emr.slgi.reception.dto.ReceptionInfo;
import com.emr.slgi.reception.dto.UpdateReceptionStatus;
import com.emr.slgi.reception.vo.UpdateReceptionStatusForm;
import com.emr.slgi.reception.dto.WaitingList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReceptionDAO {

    public int acceptPatientByStaff(ReceptionInfo receptionInfo);

    List<WaitingList> getWaitingList(String doctorUuid);

    int cancelReception(String uuid);

    int updateReceptionStatus(UpdateReceptionStatus updateStatus);

    int updateWaitingStatusOnReception(String uuid, String updateStatus);

    int checkOnTreatmentStatusExists(String doctorUuid);

    int changeReceptionStatusToComplete(String uuid);
}
