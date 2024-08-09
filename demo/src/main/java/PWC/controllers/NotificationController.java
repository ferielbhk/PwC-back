package PWC.controllers;
import PWC.entities.Notifications;
import PWC.entities.User;
import PWC.repository.NotificationRepository;
import PWC.repository.UserRepository;
import PWC.services.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private  final NotificationService notificationService ;
    private final Sinks.Many<Notifications> notificationSink = Sinks.many().multicast().onBackpressureBuffer();
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notifications> streamNotifications() {
        return notificationSink.asFlux();
    }

    public void publishNotification(Notifications event) {
        try {

            notificationSink.tryEmitNext(event);
        } catch (Exception e) {
            System.err.println("Failed to publish notification: " + e.getMessage());
        }
    }
    @GetMapping("/all")
    public List<Notifications> allNotificationsByUser(@RequestParam("id") Integer id) {
        return notificationRepository.findByUserIdReceiver(id);
    }
    @PutMapping("/mark-as-read/{id}")
    public void markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
    /*@PostMapping("/create-notification-message")
    public void createNotification(Notifications notification) {
        notification.setMessage("A new message from");
        notification.setUserIdReceiver();
        notificationRepository.save(notification);
        try {
            webSocketHandler.sendNotification(notification.getUserIdReceiver().longValue(), notification.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/





}
