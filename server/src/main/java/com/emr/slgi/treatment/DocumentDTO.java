package com.emr.slgi.treatment;


import lombok.Data;

@Data
public class DocumentDTO {
    private String treatDate;
    private String patientName;
    private String treateContent;
    private String rrn;
    private String uuid;
    private String doctorName;
}
