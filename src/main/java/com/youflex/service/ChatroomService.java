package com.youflex.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.youflex.dto.ChatMemberDTO;
import com.youflex.dto.ChatroomDTO;
import com.youflex.mapper.ChatMemberMapper;
import com.youflex.mapper.ChatroomMapper;

import lombok.RequiredArgsConstructor;

@Service // 스프링 빈으로 등록 (서비스 계층)
@RequiredArgsConstructor // final 필드를 인자로 받는 생성자를 롬복이 자동 생성 (생성자 주입)
public class ChatroomService {

    // 채팅방(chatroom) 테이블 관련 DB 접근을 담당하는 매퍼
    private final ChatroomMapper chatroomMapper;

    // 채팅방 참여자(chat_member) 테이블 관련 DB 접근을 담당하는 매퍼
    private final ChatMemberMapper chatMemberMapper;

    /**
     * 채팅방 생성 (방장 자동 입장 없이 단순 생성만 필요할 때 사용)
     */
    public int createChatroom(ChatroomDTO chatroom) {
        return chatroomMapper.createChatroom(chatroom);
    }

    /**
     * 채팅방 개설 + 개설자를 방장으로 자동 입장 (하나의 트랜잭션)
     * - 컨트롤러의 createChatroom()에서 호출됨
     */
    @Transactional
    public int createChatroomWithHost(ChatroomDTO chatroom) {
        // 1. 채팅방 생성 (insert 후 useGeneratedKeys로 chatroomId가 chatroom 객체에 채워짐)
        chatroomMapper.createChatroom(chatroom);

        // 2. 개설자를 방장으로 등록
        ChatMemberDTO host = ChatMemberDTO.builder()
                .memberId(chatroom.getMemberId())
                .chatroomId(chatroom.getChatroomId())
                .chatMemberRole("방장")
                .chatMemberStatus("참여중")
                .build();
        chatMemberMapper.insertChatMember(host);

        // 3. 생성된 채팅방 ID 반환
        return chatroom.getChatroomId();
    }

    /**
     * 채팅방 ID로 특정 채팅방 조회
     */
    public ChatroomDTO getChatroom(int chatroomId) {
        return chatroomMapper.selectChatroomById(chatroomId);
    }

    /**
     * 전체 채팅방 목록 조회
     */
    public List<ChatroomDTO> getAllChatrooms() {
        return chatroomMapper.selectAllChatrooms();
    }

    /**
     * 채팅방 정보 수정 (반환값: 수정된 row 수)
     */
    public int updateChatroom(ChatroomDTO chatroom) {
        return chatroomMapper.updateChatroom(chatroom);
    }

    /**
     * 채팅방 삭제 (반환값: 삭제된 row 수)
     */
    public int deleteChatroom(int chatroomId) {
        return chatroomMapper.deleteChatroom(chatroomId);
    }
}