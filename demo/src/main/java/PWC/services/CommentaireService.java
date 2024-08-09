package PWC.services;

import PWC.controllers.NotificationController;
import PWC.entities.*;
import PWC.repository.CommentaireRepository;
import PWC.repository.NotificationRepository;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
@AllArgsConstructor
public class CommentaireService {
    private final CommentaireRepository commentaireRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    private final RestTemplate restTemplate;
    @Autowired
    private NotificationController notificationController;
    @Autowired
    private NotificationRepository notificationRepository;

    public Commentaire createComment(Long postId, Integer userId, Commentaire commentaire) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        commentaire.setPost(post);
        commentaire.setUser(user);
        post.getComments().add(commentaire);
        post.setCommentaires(post.getCommentaires() + 1);

        Commentaire commentResult = commentaireRepository.save(commentaire);

       if (!post.getUser().getId().equals(user.getId())) {
            Notifications event = new Notifications();
            event.setPostId(postId);
            event.setUserIdSender(commentaire.getUser().getId());
            event.setMessage(" New comment on your post from "+commentaire.getUser().getFirstname()+" "+commentaire.getUser().getLastname());
            event.setTimestamp(new Date());
            event.setType(NotificationType.COMMENT);
            event.setUserIdReceiver(post.getUser().getId());
            notificationRepository.save(event);
            notificationController.publishNotification(event);
        }

        return commentResult;
    }
}
//SecurityContextHolder.getContext().getAuthentication()