package PWC.services;

import PWC.entities.ChatMessage;
import PWC.entities.Notifications;
import PWC.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    public ChatService(SimpMessagingTemplate messagingTemplate, MessageRepository messageRepository, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.notificationService = notificationService;
    }
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Message sent to /topic/messages");
        saveMessage(message);
       
    }
    @Transactional
    public void saveMessage(ChatMessage message) {
        messageRepository.save(message);

        System.out.println("Message ");
    }


}
