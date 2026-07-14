package com.youflex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.QnaDTO;
import com.youflex.dto.AdminAnswerDTO;
import com.youflex.service.QnaService;
import com.youflex.service.QnaCommentService;
import com.youflex.service.AdminAnswerService;

@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnaViewController {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final AdminAnswerService adminAnswerService;

    @GetMapping("/{qnaId}")
    public String qnaDetail(@PathVariable("qnaId") int qnaId, Model model) {
        QnaDTO qna = qnaService.getQnaDetail(qnaId);
        AdminAnswerDTO answer = adminAnswerService.getAnswer(qnaId);

        model.addAttribute("qna", qna);
        model.addAttribute("answer", answer); // null이면 답변대기 박스로 분기
        model.addAttribute("comments", qnaCommentService.getComments(qnaId));
        return "qna/qna_detail";
    }

    @GetMapping("/write")
    public String qnaWriteForm() {
        return "qna/qna_write";
    }

    // TODO: 조회수 증가 없는 순수 조회 메서드로 교체 필요 (지금은 수정 폼 진입만으로 조회수가 올라감)
    @GetMapping("/{qnaId}/edit")
    public String qnaEditForm(@PathVariable("qnaId") int qnaId, Model model) {
        QnaDTO qna = qnaService.getQnaDetail(qnaId);
        model.addAttribute("qna", qna);
        return "qna/qna_update";
    }
}
