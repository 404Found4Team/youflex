package com.youflex.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youflex.dto.ChatroomDTO;
import com.youflex.dto.MemberDTO;
import com.youflex.service.ChatroomService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatroomController {

    private final ChatroomService chatroomService;

    /**
     * 채팅방 개설 (개설자를 방장으로 자동 입장)
     * - 로그인 세션(loginMember)에서 memberId를 꺼내 채팅방 개설자로 설정
     * - [임시 조치] 세션이 없을 경우 테스트용으로 memberId=1 강제 주입
     *   (실제 로그인 붙이면 이 else 블록은 삭제할 것)
     */
    @PostMapping
    public int createChatroom(@RequestBody ChatroomDTO chatroom, HttpSession session) {

        // MemberController의 login()에서 session.setAttribute("loginMember", loginMember)로 저장한 값
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if (loginMember == null) {
            // [★ 임시 조치] 로그인이 안 되어 있다면, DB의 member 테이블에 실제 존재하는 회원 ID(예: 1)를 강제로 넣습니다.
            // 본인의 DB member 테이블에 1번 ID를 가진 회원이 없다면, 있는 회원 번호로 숫자를 바꿔주세요.
            chatroom.setMemberId(1);
            System.out.println("임시 경고: 로그인 세션이 없어 member_id를 강제로 1로 설정하여 진행합니다.");
        } else {
            chatroom.setMemberId(loginMember.getMemberId());
        }

        // DB에 정상적으로 insert가 수행되고 새로 생성된 방 번호(int)가 프론트엔드로 반환됨
        return chatroomService.createChatroomWithHost(chatroom);
    }

    // 특정 채팅방 조회
    @GetMapping("/{chatroomId}")
    public ChatroomDTO getChatroom(@PathVariable int chatroomId) {
        return chatroomService.getChatroom(chatroomId);
    }

    // 전체 채팅방 목록 조회
    @GetMapping
    public List<ChatroomDTO> getAllChatrooms() {
        return chatroomService.getAllChatrooms();
    }

    // 채팅방 정보 수정
    @PutMapping
    public int updateChatroom(@RequestBody ChatroomDTO chatroom) {
        return chatroomService.updateChatroom(chatroom);
    }

    // 채팅방 삭제
    @DeleteMapping("/{chatroomId}")
    public int deleteChatroom(@PathVariable int chatroomId) {
        return chatroomService.deleteChatroom(chatroomId);
    }
}