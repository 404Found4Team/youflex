package com.youflex.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.QnaCommentDTO;
import com.youflex.mapper.QnaCommentMapper;

@Service
@RequiredArgsConstructor
public class QnaCommentServiceImpl implements QnaCommentService {

    private final QnaCommentMapper qnaCommentMapper;

    @Override
    public List<QnaCommentDTO> getComments(int qnaId) {
        return qnaCommentMapper.selectCommentsByQnaId(qnaId);
    }

    @Override
    public void addComment(QnaCommentDTO commentDTO) {
        qnaCommentMapper.insertComment(commentDTO);
    }

    @Override
    public void deleteComment(int qnaCommentId, int requesterMemberId) {
        QnaCommentDTO comment = qnaCommentMapper.selectCommentById(qnaCommentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        // 작성자 본인 또는 관리자만 삭제 가능 - 관리자 체크는 Controller/Security에서 별도 처리 필요
        if (comment.getMemberId() != requesterMemberId) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        qnaCommentMapper.deleteComment(qnaCommentId);
    }
}
