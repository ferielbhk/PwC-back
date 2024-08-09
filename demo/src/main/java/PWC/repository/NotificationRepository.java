package PWC.repository;

import PWC.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByUserIdReceiver(Integer id);
    //List<Notifications> findByReceiverId(Integer userId);

   // List<Notifications> findByReceiverIdAndSeenFalse(Integer userId);
}
