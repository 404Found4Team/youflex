package com.youflex.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.AdminAnswerDTO;
import com.youflex.mapper.AdminAnswerMapper;
import com.youflex.mapper.QnaMapper;

@Service
@RequiredArgsConstructor
public class AdminAnswerServiceImpl implements AdminAnswerService {

    private final AdminAnswerMapper adminAnswerMapper;
    private final QnaMapper qnaMapper;

    @Override
    public AdminAnswerDTO getAnswer(int qnaId) {
        return adminAnswerMapper.selectAnswerByQnaId(qnaId);
    }

    @Override
    @Transactional
    public void saveAnswer(int qnaId, String content) {
        AdminAnswerDTO existing = adminAnswerMapper.selectAnswerByQnaId(qnaId);
        if (existing == null) {
            AdminAnswerDTO answer = AdminAnswerDTO.builder()
                    .qnaId(qnaId)
                    .adminAnswerContent(content)
                    .build();
            adminAnswerMapper.insertAnswer(answer);
        } else {
            existing.setAdminAnswerContent(content);
            adminAnswerMapper.updateAnswer(existing);
        }
        // 답변 등록/수정 시 qna 상태를 답변완료로 전환
        qnaMapper.updateQnaStatus(qnaId, "DONE");
    }

    @Override
    @Transactional
    public void deleteAnswer(int adminAnswerId) {
        adminAnswerMapper.deleteAnswer(adminAnswerId);
    }
}
