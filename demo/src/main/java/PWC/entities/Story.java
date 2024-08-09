package PWC.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] image;
    private Date dateAjout;
    private Boolean read = false;
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;
}
