package com.youflex.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.QnaDTO;
import com.youflex.dto.QnaCommentDTO;
import com.youflex.dto.QnaReportDTO;
import com.youflex.service.QnaService;
import com.youflex.service.QnaCommentService;
import com.youflex.service.QnaReportService;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final QnaReportService qnaReportService;

    // ---- 질문 ----
    @GetMapping
    public ResponseEntity<List<QnaDTO>> getQnaList() {
        return ResponseEntity.ok(qnaService.getQnaList());
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDTO> getQnaDetail(@PathVariable("qnaId") int qnaId) {
        return ResponseEntity.ok(qnaService.getQnaDetail(qnaId));
    }

    @PostMapping
    public ResponseEntity<Void> createQna(@RequestBody QnaDTO qnaDTO) {
        // TODO: memberId는 세션에서 꺼내서 세팅 (현재는 요청 바디 신뢰 - 실제 적용 전 수정 필요)
        qnaService.createQna(qnaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{qnaId}")
    public ResponseEntity<Void> updateQna(@PathVariable("qnaId") int qnaId, @RequestBody QnaDTO qnaDTO) {
        qnaDTO.setQnaId(qnaId);
        qnaService.updateQna(qnaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQna(@PathVariable("qnaId") int qnaId) {
        qnaService.deleteQna(qnaId);
        return ResponseEntity.noContent().build();
    }

    // ---- 댓글 ----
    @GetMapping("/{qnaId}/comments")
    public ResponseEntity<List<QnaCommentDTO>> getComments(@PathVariable("qnaId") int qnaId) {
        return ResponseEntity.ok(qnaCommentService.getComments(qnaId));
    }

    @PostMapping("/{qnaId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("qnaId") int qnaId, @RequestBody QnaCommentDTO commentDTO) {
        commentDTO.setQnaId(qnaId);
        qnaCommentService.addComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/comments/{qnaCommentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("qnaCommentId") int qnaCommentId,
                                               @RequestParam int memberId) {
        qnaCommentService.deleteComment(qnaCommentId, memberId);
        return ResponseEntity.noContent().build();
    }

    // ---- 질문 신고 ----
    @PostMapping("/{qnaId}/report")
    public ResponseEntity<Void> reportQna(@PathVariable("qnaId") int qnaId, @RequestBody QnaReportDTO reportDTO) {
        reportDTO.setQnaId(qnaId);
        qnaReportService.reportQna(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
