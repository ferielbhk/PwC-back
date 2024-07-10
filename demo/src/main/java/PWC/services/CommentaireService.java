package PWC.services;

import PWC.entities.Commentaire;
import PWC.entities.Post;
import PWC.entities.User;
import PWC.repository.CommentaireRepository;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CommentaireService {
    private final CommentaireRepository commentaireRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;



    public Commentaire createComment(Long postId, Integer userId, Commentaire commentaire) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            commentaire.setPost(post);
            commentaire.setUser(user);
            post.getComments().add(commentaire);
            post.setCommentaires(post.getCommentaires() + 1);
            return commentaireRepository.save(commentaire);
}

    public List<Commentaire> getCommentsByPost(Long postId) {
        return commentaireRepository.findByPostId(postId);
    }

    public Commentaire updateComment(Long commentId, String newContent) {
        Commentaire commentaire = commentaireRepository.findById(commentId).get();

           commentaire.setContent(newContent);
            return commentaireRepository.save(commentaire);

    }

    public void deleteComment(Long commentId) {
       Commentaire commentaire= commentaireRepository.findById(commentId).get();
            Post post = commentaire.getPost();
            post.getComments().remove(commentaire);
            post.setCommentaires(post.getCommentaires() - 1);
            commentaireRepository.delete(commentaire);
        }
    }


