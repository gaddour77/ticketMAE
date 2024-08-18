package tn.esprit.ticketmaeassurrance.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.ticketmaeassurrance.Dto.ChatMessage;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class WebSocketController {
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId,ChatMessage chatMessage){
        return new ChatMessage(chatMessage.getMessage(),chatMessage.getUser());
    }
    @MessageMapping("/chat/{roomId}/screen")
    @SendTo("/topic/{roomId}/screen")
    public String handleScreenShare(@DestinationVariable String roomId,String screenData) {
        return screenData;  // Simply forward the screen data to subscribers
    }
}
