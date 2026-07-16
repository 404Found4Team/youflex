package com.youflex.mapper.qna;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.youflex.dto.qna.QnaDTO;

@Mapper
public interface QnaMapper {
    List<QnaDTO> selectQnaList();
    QnaDTO selectQnaById(int qnaId);
    void increaseQnaHit(int qnaId);
    void insertQna(QnaDTO qnaDTO);
    void updateQna(QnaDTO qnaDTO);
    void updateQnaStatus(@Param("qnaId") int qnaId, @Param("qnaStatus") String qnaStatus);
    void deleteQna(int qnaId);
}
