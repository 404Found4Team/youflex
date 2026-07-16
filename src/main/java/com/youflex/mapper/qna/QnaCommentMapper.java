package com.youflex.mapper.qna;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.youflex.dto.qna.QnaCommentDTO;

@Mapper
public interface QnaCommentMapper {
    List<QnaCommentDTO> selectCommentsByQnaId(int qnaId);
    void insertComment(QnaCommentDTO commentDTO);
    QnaCommentDTO selectCommentById(int qnaCommentId);
    void updateComment(QnaCommentDTO commentDTO);
    void deleteComment(int qnaCommentId);
}
