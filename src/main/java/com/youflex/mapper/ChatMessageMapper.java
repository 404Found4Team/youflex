package com.youflex.mapper;
import com.youflex.dto.ChatMessageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface ChatMessageMapper {
    
    /** 메시지 저장. useGeneratedKeys로 chatMessageId가 채워진다. */
    int insertChatMessage(ChatMessageDTO chatMessage);
 
    /** 특정 방의 메시지 내역 (오래된 순). member_loginid를 memberName 자리에 조인해서 채워온다.
     *  ★ memberId가 그 방에 "현재" 입장한 시각(chat_member_created_at) 이후 메시지만 반환된다 -
     *  입장하기 전 대화 내용은 영구히 보이지 않아야 하므로, 나갔다가 재입장해도 동일하게 적용된다. */
    List<ChatMessageDTO> getMessagesByChatroomId(@Param("chatroomId") int chatroomId, @Param("memberId") int memberId);
 
    /**
     * 실시간 메시지 브로드캐스트 시 로그인아이디를 채워 보내기 위한 단건 조회.
     * (insertChatMessage 직후엔 memberName이 안 채워져 있으므로 별도로 조회해서 채운다)
     */
	String selectMemberName(@Param("memberId") int memberId);

    /** ★ 추가: 경고 부여 시 대상 메시지의 chatroomId/memberId를 확인하기 위한 단건 조회 */
    ChatMessageDTO selectChatMessageById(@Param("chatMessageId") int chatMessageId);
}