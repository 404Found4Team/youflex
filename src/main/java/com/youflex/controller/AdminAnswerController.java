package com.youflex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.AdminAnswerDTO;
import com.youflex.service.AdminAnswerService;

@RestController
@RequestMapping("/api/qna/{qnaId}/answer")
@RequiredArgsConstructor
public class AdminAnswerController {

    private final AdminAnswerService adminAnswerService;

    @GetMapping
    public ResponseEntity<AdminAnswerDTO> getAnswer(@PathVariable("qnaId")int qnaId) {
        AdminAnswerDTO answer = adminAnswerService.getAnswer(qnaId);
        if (answer == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(answer);
    }

    // 답변 등록/수정 겸용 - 관리자 전용
    @PostMapping
    public ResponseEntity<Void> saveAnswer(@PathVariable("qnaId") int qnaId, @RequestBody AnswerRequest request) {
        // TODO: 관리자 권한 체크 필요
        if (request.getContent() == null || request.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        adminAnswerService.saveAnswer(qnaId, request.getContent());
        return ResponseEntity.ok().build();
    }

    // 답변 삭제 - 관리자 전용
    @DeleteMapping("/{adminAnswerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable("qnaId") int qnaId, @PathVariable("adminAnswerId") int adminAnswerId) {
        // TODO: 관리자 권한 체크 필요
        adminAnswerService.deleteAnswer(adminAnswerId);
        return ResponseEntity.noContent().build();
    }

    @Data
    static class AnswerRequest {
        private String content;
    }
}

