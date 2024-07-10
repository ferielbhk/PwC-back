// Post.java
package PWC.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4500)
    private String content;
    private int likes = 0;
    private int commentaires;

    @Lob
    private byte[] image;


    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @ManyToMany(mappedBy = "likedPosts")
    private List<User> likedUsers;

    @OneToMany
    private List<Commentaire> comments;
}
