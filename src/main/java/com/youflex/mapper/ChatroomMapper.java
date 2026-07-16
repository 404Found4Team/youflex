package com.youflex.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.youflex.dto.ChatroomDTO;

@Mapper // MyBatis 매퍼 인터페이스로 등록 (구현체는 XML 매핑 파일이 대신함)
public interface ChatroomMapper {

    /**
     * 채팅방 생성
     * - INSERT 실행, useGeneratedKeys 설정 시 생성된 chatroomId가
     *   파라미터로 넘긴 chatroom 객체에 자동으로 채워짐
     * - 반환값: DB에 반영된 row 수 (보통 성공 시 1)
     */
    int createChatroom(ChatroomDTO chatroom);

    /**
     * 채팅방 ID로 단건 조회
     * - 존재하지 않는 chatroomId면 null 반환
     */
    ChatroomDTO selectChatroomById(int chatroomId);

    /**
     * 전체 채팅방 목록 조회
     */
    List<ChatroomDTO> selectAllChatrooms();

    /**
     * 채팅방 정보 수정
     * - chatroom 객체 안의 chatroomId를 기준으로 UPDATE
     * - 반환값: 수정된 row 수 (보통 성공 시 1, 해당 ID 없으면 0)
     */
    int updateChatroom(ChatroomDTO chatroom);

    /**
     * 채팅방 삭제
     * - 반환값: 삭제된 row 수 (보통 성공 시 1, 해당 ID 없으면 0)
     */
    int deleteChatroom(int chatroomId);

}