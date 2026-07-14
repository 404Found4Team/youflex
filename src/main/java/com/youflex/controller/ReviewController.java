package com.youflex.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.youflex.dto.MemberDTO;
import com.youflex.dto.ReviewDTO;
import com.youflex.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	
//	1) 작성 폼으로 이동
	@GetMapping("/review/write")
	public String writeForm(HttpSession session, Model model) {
//		세션에 loginMember가 없으면 => 로그인 페이지로 이동
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/login";
		}
		
		return "review/write";
	}
	
//	2) 리뷰 글 작성
	@PostMapping("/review/write")
	public String write(ReviewDTO reviewDTO, HttpSession session) throws IOException{
//		로그인 여부 확인
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/login";
		}
		
//		세션에서 로그인 회원 정보 꺼내기(작성자 정보)
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		
//		ReviewDTO에 작성자 번호(memberId) 설정
		reviewDTO.setMemberId(loginMember.getMemberId());
		//System.out.println(reviewDTO.getMemberId());
		
//		if(reviewDTO.getImgFile() != )
		
//		저장 완료 후 메인 화면으로 이동
		return "redirect:/";
	}
}
