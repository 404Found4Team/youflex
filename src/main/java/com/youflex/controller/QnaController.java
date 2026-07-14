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

/**
 * Q&A(질문/답변) 관련 API 컨트롤러
 * - 질문 CRUD, 댓글 CRUD, 질문 신고 기능을 포함
 */
@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final QnaReportService qnaReportService;

    // ---- 질문 ----

    /**
     * 전체 질문 목록 조회
     * @return 질문 리스트 (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<QnaDTO>> getQnaList() {
        return ResponseEntity.ok(qnaService.getQnaList());
    }

    /**
     * 특정 질문 상세 조회
     * @param qnaId 조회할 질문 ID
     * @return 질문 상세 정보 (200 OK)
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDTO> getQnaDetail(@PathVariable("qnaId") int qnaId) {
        return ResponseEntity.ok(qnaService.getQnaDetail(qnaId));
    }

    /**
     * 질문 등록
     * @param qnaDTO 등록할 질문 정보 (요청 바디)
     * @return 등록 성공 시 201 Created
     */
    @PostMapping
    public ResponseEntity<Void> createQna(@RequestBody QnaDTO qnaDTO) {
        // TODO: memberId는 세션에서 꺼내서 세팅 (현재는 요청 바디 신뢰 - 실제 적용 전 수정 필요)
        // -> 클라이언트가 임의의 memberId를 조작해서 보낼 수 있는 보안 취약점 존재
        qnaService.createQna(qnaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 질문 수정
     * @param qnaId 수정할 질문 ID (경로 변수)
     * @param qnaDTO 수정할 내용을 담은 요청 바디
     * @return 수정 성공 시 200 OK
     */
    @PutMapping("/{qnaId}")
    public ResponseEntity<Void> updateQna(@PathVariable("qnaId") int qnaId, @RequestBody QnaDTO qnaDTO) {
        // 경로 변수의 qnaId를 DTO에 강제로 세팅하여 요청 바디 값과의 불일치 방지
        qnaDTO.setQnaId(qnaId);
        qnaService.updateQna(qnaDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 질문 삭제
     * @param qnaId 삭제할 질문 ID
     * @return 삭제 성공 시 204 No Content
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQna(@PathVariable("qnaId") int qnaId) {
        // TODO: 작성자 본인 또는 관리자만 삭제 가능하도록 권한 체크 필요
        qnaService.deleteQna(qnaId);
        return ResponseEntity.noContent().build();
    }

    // ---- 댓글 ----

    /**
     * 특정 질문에 달린 댓글 목록 조회
     * @param qnaId 질문 ID
     * @return 댓글 리스트 (200 OK)
     */
    @GetMapping("/{qnaId}/comments")
    public ResponseEntity<List<QnaCommentDTO>> getComments(@PathVariable("qnaId") int qnaId) {
        return ResponseEntity.ok(qnaCommentService.getComments(qnaId));
    }

    /**
     * 특정 질문에 댓글 등록
     * @param qnaId 댓글을 달 질문 ID
     * @param commentDTO 등록할 댓글 정보 (요청 바디)
     * @return 등록 성공 시 201 Created
     */
    @PostMapping("/{qnaId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("qnaId") int qnaId, @RequestBody QnaCommentDTO commentDTO) {
        // 경로 변수의 qnaId를 DTO에 세팅하여 어느 질문에 대한 댓글인지 명확히 함
        commentDTO.setQnaId(qnaId);
        qnaCommentService.addComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 댓글 삭제
     * @param qnaCommentId 삭제할 댓글 ID
     * @param memberId 요청자 회원 ID (본인 댓글인지 확인용, 쿼리 파라미터)
     * @return 삭제 성공 시 204 No Content
     */
    @DeleteMapping("/comments/{qnaCommentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("qnaCommentId") int qnaCommentId,
                                               @RequestParam int memberId) {
        // TODO: memberId를 요청 파라미터로 그대로 받는 방식은 위변조 가능성 있음 - 세션 기반 검증 권장
        qnaCommentService.deleteComment(qnaCommentId, memberId);
        return ResponseEntity.noContent().build();
    }

    // ---- 질문 신고 ----

    /**
     * 질문 신고 등록
     * @param qnaId 신고 대상 질문 ID
     * @param reportDTO 신고 사유 등을 담은 요청 바디
     * @return 등록 성공 시 201 Created
     */
    @PostMapping("/{qnaId}/report")
    public ResponseEntity<Void> reportQna(@PathVariable("qnaId") int qnaId, @RequestBody QnaReportDTO reportDTO) {
        reportDTO.setQnaId(qnaId);
        qnaReportService.reportQna(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}