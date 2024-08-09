package PWC.services;
import PWC.entities.Notifications;
import PWC.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
@Service
public class NotificationService {
    private  final NotificationRepository notificationRepository;
    private final Sinks.Many<Notifications> notificationsSink = Sinks.many().multicast().onBackpressureBuffer();

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    @Async
    public void sendNotification(Integer receiverId , Notifications notification) {
        System.out.println("Notification sent: " + notification);
        notificationsSink.tryEmitNext(notification);
    }
    public void markAsRead(Long id) {
        Notifications notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public Flux<Notifications> getNotifications() {
        return notificationsSink.asFlux();
    }







}
