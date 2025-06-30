package com.emr.slgi.page;

import lombok.Data;

@Data
public class PageDTO {
	private int totalCount;  // 전체 데이터 수
    private int totalPage;   // 전체 페이지 수
    private int pageNo;      // 현재 페이지
    private int size;        // 한 페이지당 개수
    private int startPage;   // 페이지 네비게이션 시작 번호
    private int endPage;     // 페이지 네비게이션 끝 번호
    private boolean prev;    // 이전 페이지 존재 여부
    private boolean next;    // 다음 페이지 존재 여부

    public PageDTO(int totalCount, int pageNo, int size) {
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.size = size;
        
        this.totalPage = (int) Math.ceil((double) totalCount / size);
        int page = 5;
        this.startPage = ((pageNo - 1) / page) * page + 1;
        this.endPage = Math.min(startPage + page - 1, totalPage);

        this.prev = startPage > 1;
        this.next = endPage < totalPage;
    }

}
