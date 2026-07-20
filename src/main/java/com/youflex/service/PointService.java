package com.youflex.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youflex.dto.PointHistoryDTO;
import com.youflex.mapper.MemberMapper;
import com.youflex.mapper.PointHistoryMapper;

import lombok.RequiredArgsConstructor;

// 여러 도메인(퀴즈 정답, 좋아요 등)이 공용으로 쓰는 포인트 적립 서비스.
@Service
@RequiredArgsConstructor
public class PointService {

    private final MemberMapper memberMapper;
    private final PointHistoryMapper pointHistoryMapper;

    // 포인트 적립 - member.member_point 증가 + point_history에 '적립' 이력 기록을 한 트랜잭션으로 처리
    @Transactional
    public void awardPoints(int memberId, int amount, String reason) {
        memberMapper.addPoint(memberId, amount);
        pointHistoryMapper.insertPointHistory(PointHistoryDTO.builder()
                .memberId(memberId)
                .pointHistoryAmount(amount)
                .pointHistoryType("적립")
                .pointHistoryReason(reason)
                .build());
    }
}
