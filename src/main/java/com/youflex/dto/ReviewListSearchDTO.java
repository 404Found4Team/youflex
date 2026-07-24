package com.youflex.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReviewListSearchDTO {
	private String keyword;
    private String period;              // all, 1w, 1m, 3m
    private String platform;            // netflix, tving, disney, etc
    private String sort;                // latest, rating, views (likes는 아직 미지원 -> latest로 폴백)
    private boolean highlightOnly;       // [추가] '하이라이트만 보기' 여부 - sort(정렬 기준)와 분리된 필터라서 최신순/좋아요순/조회수순으로 바꿔도 유지됨
    private List<Integer> genreCategoryIds;
    private int page = 1;
    private int size;                   // Service에서 세팅
    private int offset;                 // Service에서 세팅
}
