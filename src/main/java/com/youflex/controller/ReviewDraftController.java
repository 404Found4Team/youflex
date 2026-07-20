package com.youflex.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youflex.dto.MemberDTO;
import com.youflex.dto.ReviewDraftDTO;
import com.youflex.service.ReviewDraftService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewDraftController {
	
	private final ReviewDraftService draftService;
	
	// 1. 임시저장
	@PostMapping("/review/draft/save")
	public ResponseEntity<String> saveDraft(ReviewDraftDTO draftDTO, HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}
		
		draftDTO.setMemberId(loginMember.getMemberId());
		draftService.saveDraft(draftDTO);
		
		return ResponseEntity.ok("SUCCESS");
	}

	// 2. 임시저장 목록 조회
	@GetMapping("/review/draft/list")
	public ResponseEntity<?> getDraftList(HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		// int / Long 타입 변환 안전하게 처리 (500 에러 방지)
		int memberId = (int) loginMember.getMemberId();

		ReviewDraftDTO draft = draftService.getDraftByMemberId(memberId);
		List<ReviewDraftDTO> list = new ArrayList<>();
		
		if (draft != null) {
			list.add(draft);
		}
		
		return ResponseEntity.ok(list);
	}

	// 3. 임시저장 상세 조회 (불러오기)
	@GetMapping("/review/draft/detail")
	public ResponseEntity<?> getDraftDetail(HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		int memberId = (int) loginMember.getMemberId();

		ReviewDraftDTO draft = draftService.getDraftByMemberId(memberId);
		return ResponseEntity.ok(draft);
	}

	// 4. 임시저장 삭제
	@DeleteMapping("/review/draft/delete")
	public ResponseEntity<String> deleteDraft(HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		int memberId = (int) loginMember.getMemberId();

		draftService.deleteDraftByMemberId(memberId);
		return ResponseEntity.ok("SUCCESS");
	}
}