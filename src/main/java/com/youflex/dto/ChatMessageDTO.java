package com.youflex.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    // ★ 실시간 전송(STOMP SEND)에서는 클라이언트가 아직 DB에 없는 chatMessageId를 보내지 않으므로,
    //   기본형 int로 두면 Jackson이 null을 int로 변환하지 못해 역직렬화 자체가 실패한다 - Integer로 유지
    private Integer chatMessageId;
    private int chatroomId;
    private int memberId;
    private String chatMessageContent;
    private LocalDateTime chatMessageCreatedAt;

    // join 조회용 (DB 컬럼 아님)
    private String memberName;
    private String memberLoginId;
    private String memberProfileImg;
}
