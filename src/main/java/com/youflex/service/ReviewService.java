package com.youflex.service;

import org.springframework.stereotype.Service;

import com.youflex.mapper.BookmarkMapper;
import com.youflex.mapper.CommentMapper;
import com.youflex.mapper.CommentLikeMapper;
import com.youflex.mapper.ReviewMapper;
import com.youflex.mapper.ReviewReportMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewMapper reviewMapper;
	private final CommentMapper commentMapper;
	private final CommentLikeMapper likeMapper;
	private final BookmarkMapper bookmarkMapper;
	private final ReviewReportMapper reviewReportMapper;
}
