package PWC.controllers;

import PWC.entities.ChatMessage;
import PWC.entities.NotificationType;
import PWC.entities.Notifications;
import PWC.repository.MessageRepository;
import PWC.repository.NotificationRepository;
import PWC.repository.UserRepository;
import PWC.services.ChatService;
import PWC.services.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;
    private  final UserService userService;
    private final UserRepository userRepository;
    public ChatController(ChatService chatService, MessageRepository messageRepository, NotificationRepository notificationRepository, UserService userService, UserRepository userRepository) {
        this.chatService = chatService;
        this.messageRepository = messageRepository;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessage message) {
        System.out.println(message);
        chatService.sendMessage(message);

        System.out.println(message);
    }

    @MessageMapping("/chat-message")
    @SendTo("/topic/chat-messages")
    public ChatMessage handleChatMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) throws IOException {
        chatService.sendMessage(message);
        Notifications notification = new Notifications();

        notification.setMessage("A new message from "+(userRepository.findById(message.getSenderId().intValue())).get().getFirstname() + ( userRepository.findById(message.getSenderId().intValue())).get().getLastname());
        notification.setTimestamp(new Date());
        notification.setUserIdReceiver(message.getReceiverId().intValue());
        notification.setUserIdSender(message.getSenderId().intValue());
        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);

       // webSocketHandler.sendNotification(notification.getUserIdReceiver(), notification.getMessage());

        System.out.println(notification);
        System.out.println("msgNotif"+notification.getMessage());

        return message;

    }

    @MessageMapping("/fetch-messages")
    @SendTo("/topic/messages")
    public List<ChatMessage> fetchMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/api/messages")
    @ResponseBody
    public List<ChatMessage> getAllMessages() {
        return messageRepository.findAll();
    }
}
