package com.youflex.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youflex.dto.NoticeDTO;
import com.youflex.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeViewController {

    private final NoticeService noticeService;

    // 공지사항 목록 화면
    @GetMapping
    public String noticeList(Model model) {
        List<NoticeDTO> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "notice/notice"; 
    }

    // 공지사항 상세 화면
    @GetMapping("/{noticeId}")
    public String noticeDetail(@PathVariable("noticeId") int noticeId, Model model) {
        NoticeDTO notice = noticeService.getNoticeDetail(noticeId);
        model.addAttribute("notice", notice);
        return "notice/notice_detail"; 
    }
}
