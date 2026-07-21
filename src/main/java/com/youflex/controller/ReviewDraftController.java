package com.youflex.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@PostMapping("/review/draft/save")
	public ResponseEntity<String> saveDraft(ReviewDraftDTO draftDTO, HttpSession session){
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		if(loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}
		// 로그인된 회원 ID 세팅
		int memberId = (int)loginMember.getMemberId();
		draftDTO.setMemberId(memberId);

		
		String result = draftService.saveOrUpdateDraft(draftDTO);
		
		return ResponseEntity.ok(result);
	}

	// 2. 임시저장 목록 조회(최대 5개)
	@GetMapping("/review/draft/list")
	public ResponseEntity<?> getDraftList(HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		int memberId = (int) loginMember.getMemberId();
		List<ReviewDraftDTO> list = draftService.getDraftList(memberId);
		return ResponseEntity.ok(list);
	}

	// 3. 임시저장 상세 조회 (불러오기)
	@GetMapping("/review/draft/detail/{draftId}")
	public ResponseEntity<?> getDraftDetail(@PathVariable("draftId") int draftId, HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		ReviewDraftDTO draft = draftService.getDraftDetail(draftId);
		return ResponseEntity.ok(draft);
	}

	// 4. 임시저장 삭제
	public ResponseEntity<String> deleteDraft(@PathVariable("draftId") int draftId, HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return ResponseEntity.status(401).body("UNAUTHORIZED");
		}

		boolean isDeleted = draftService.deleteDraft(draftId);
		if(isDeleted) {
			return ResponseEntity.ok("SUCCESS");
		} else {
			return ResponseEntity.status(500).body("FAIL");
		}
	}
}