package PWC.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*private Integer idSender;
      private Integer idReceiver;
      @Enumerated(EnumType.STRING)
      private NotificationType type;
      private Long idPost;
      private LocalDateTime timestamp;
      private String firstNameSender;
      private String lastNameSender;*/
    private String message;
    private Date timestamp;
    private Long postId;
    private Integer userIdReceiver;
    private Integer userIdSender;
    private boolean read = false;
    @Enumerated(EnumType.STRING)
    private NotificationType type;



}