package com.youflex.service;

import org.springframework.transaction.annotation.Transactional;

import com.youflex.dto.ReviewDraftDTO;
import com.youflex.mapper.ReviewDraftMapper;

public class ReviewDraftService {
	private final ReviewDraftMapper draftMapper;
	
	@Transactional
	public void saveDraft(ReviewDraftDTO draftDTO) {
		draftMapper.deleteDraftByMemberId(draftDTO.getMemberId());
		
	}
}
