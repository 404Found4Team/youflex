

package com.youflex.controller; // 웹소켓 '컨트롤러'이므로 반드시 controller 패키지에 위치해야 합니다.

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.youflex.dto.ChatMessageDTO;

@Controller // 이 클래스가 웹소켓 메시지를 받아서 처리하는 컨트롤러 클래스임을 선언합니다.
public class WebSocketChatController {

    // [클라이언트의 발신 요청을 수신]
    // 프론트엔드에서 아까 설정한 접두사(/pub)를 붙여서 "/pub/chat/message" 주소로 
    // 채팅 메시지(JSON 데이터)를 보내면, 스프링이 이 메서드를 자동으로 실행시킵니다.
    @MessageMapping("/chat/message")
    
    // [다른 구독자들에게 메시지를 전달(배포)]
    // 이 메서드가 처리를 끝내고 return하는 결과물(ChatMessageDTO)을
    // "/sub/chatroom/{roomId}" 주소를 구독(대기)하고 있는 같은 방의 다른 유저들에게 실시간으로 복사해서 뿌려줍니다.
    // {roomId} 부분은 프론트엔드가 보낸 데이터 안에 들어있는 방 번호로 알아서 치환됩니다.
    @SendTo("/sub/chatroom/{roomId}")
    public ChatMessageDTO broadcastMessage(ChatMessageDTO message) {
        
        // [비동기 로직 영역]
        // 만약 채팅 내용을 데이터베이스에 저장하고 싶다면, 아래 주석을 풀고 서비스 레이어를 호출하면 됩니다.
        // 예: chatService.saveMessage(message); 
        
        // 매개변수로 받은 메시지 객체를 그대로 리턴합니다. 
        // 리턴된 데이터는 스프링이 자동으로 JSON 형식으로 변환하여 구독자들에게 전송합니다.
        return message;
    }
}