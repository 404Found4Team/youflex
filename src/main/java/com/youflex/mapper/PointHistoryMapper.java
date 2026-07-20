package com.youflex.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.youflex.dto.PointHistoryDTO;

@Mapper
public interface PointHistoryMapper {

    // 포인트 적립/차감 이력 저장(마이페이지 "포인트 내역" 탭 등에서 나중에 조회할 때 사용)
    void insertPointHistory(PointHistoryDTO pointHistoryDTO);
}
