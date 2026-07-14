package com.youflex.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.QnaDTO;
import com.youflex.mapper.QnaMapper;

@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaMapper qnaMapper;

    @Override
    public List<QnaDTO> getQnaList() {
        return qnaMapper.selectQnaList();
    }

    @Override
    public QnaDTO getQnaDetail(int qnaId) {
        QnaDTO qna = qnaMapper.selectQnaById(qnaId);
        if (qna == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다. qnaId=" + qnaId);
        }
        qnaMapper.increaseQnaHit(qnaId);
        qna.setQnaHit(qna.getQnaHit() + 1);
        return qna;
    }

    @Override
    public void createQna(QnaDTO qnaDTO) {
        // 1. 'Y'이면 '비밀', 아니면 '공개'로 변환 (DB의 ENUM 값과 일치시키기 위함)
        String inputSecret = qnaDTO.getQnaIsSecret();
        String finalSecret = "Y".equals(inputSecret) ? "비밀" : "공개";
        
        // 2. 변환된 값을 DTO에 다시 저장
        qnaDTO.setQnaIsSecret(finalSecret);
        
        // 3. 매퍼 호출 (매퍼 XML은 단순 삽입문으로 변경해야 함)
        qnaMapper.insertQna(qnaDTO);
    }

    @Override
    public void updateQna(QnaDTO qnaDTO) {
        QnaDTO existing = qnaMapper.selectQnaById(qnaDTO.getQnaId());
        if (existing == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다. qnaId=" + qnaDTO.getQnaId());
        }
        qnaMapper.updateQna(qnaDTO);
    }

    @Override
    public void deleteQna(int qnaId) {
        qnaMapper.deleteQna(qnaId);
    }

    @Override
    public void answerQna(int qnaId) {
        qnaMapper.updateQnaStatus(qnaId, "DONE");
    }
}
