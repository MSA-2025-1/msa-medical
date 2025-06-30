package com.emr.slgi.page;

import java.util.List;

import lombok.Data;

@Data
public class PageResponseDTO<T> {
    private PageDTO pageInfo;  // 페이지 정보
    private List<T> list;      // 실제 데이터 리스트

    public PageResponseDTO(PageDTO pageInfo, List<T> list) {
        this.pageInfo = pageInfo;
        this.list = list;
    }
}

