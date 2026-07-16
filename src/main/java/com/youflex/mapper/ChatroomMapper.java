package com.youflex.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.youflex.dto.ChatroomDTO;
@Mapper
public interface ChatroomMapper {

    int createChatroom(ChatroomDTO chatroom);

    ChatroomDTO selectChatroomById(int chatroomId);

    List<ChatroomDTO> selectAllChatrooms();

    int updateChatroom(ChatroomDTO chatroom);

    int deleteChatroom(int chatroomId);

    /**
     * 특정 회원이 개설한 채팅방 개수 조회
     * - 1개 이상이면 이미 개설한 방이 있는 것으로 판단
     */
    int countChatroomByMemberId(int memberId);
}