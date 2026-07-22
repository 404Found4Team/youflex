package com.youflex.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatWarningMapper {

    /** 경고 기록 저장 */
    int insertWarning(@Param("memberId") int memberId,
                       @Param("chatroomId") int chatroomId,
                       @Param("chatMessageId") int chatMessageId,
                       @Param("reason") String reason);

    /** 특정 방에서 특정 회원이 받은 누적 경고 횟수 */
    int countWarnings(@Param("chatroomId") int chatroomId,
                       @Param("memberId") int memberId);
}