package com.youflex.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewDTO {
    private int reviewId;
    private int memberId;
    private int genreCategoryId;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImg;
    private String reviewRelated;
    private int reviewHit;
    private Double reviewRating;
    private String reviewPlatform;
    private Boolean reviewHighlighted;
    private LocalDateTime reviewCreatedAt;
    private LocalDateTime reviewUpdatedAt;
}
