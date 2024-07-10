package PWC.services;

import PWC.entities.Post;
import PWC.entities.User;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public void likePost(long postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!post.getLikedUsers().contains(user)) {
            post.getLikedUsers().add(user);
            //post.setLiked(true);
            post.setLikes(post.getLikes()+1);
            postRepository.save(post);

        }
    }
   public void unlikePost(long postId, int userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("User not found"));
       if (post.getLikedUsers().contains(user)) {
            post.getLikedUsers().remove(user);
            post.setLikes(post.getLikes()-1);
            //post.setLiked(false);
            postRepository.save(post);
        }
    }
}