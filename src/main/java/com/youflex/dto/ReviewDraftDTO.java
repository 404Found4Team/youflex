package com.youflex.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDraftDTO {
    private int reviewDraftId;
    private int memberId;
    private List<Integer> genreCategoryId;
    private String reviewDraftTitle;
    private String reviewDraftContent;
    private LocalDateTime reviewDraftSavedAt;

    // 여러 장르명이 한 번에 넘어올 수 있도록 List로 구현
    private List<String> genreCategoryName;
}
