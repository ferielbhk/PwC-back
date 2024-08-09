package PWC.services;
import PWC.entities.*;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public void likePost(long postId, int userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userOptional.get();

            if (!post.getLikedUsers().contains(user)) {
                post.getLikedUsers().add(user);
                user.getLikedPosts().add(post);
                post.setLikes(post.getLikes() + 1);


                Notifications notification = new Notifications();

                notificationService.sendNotification(post.getUser().getId(), notification);
                System.out.println("notification send succefully from Post"+notification);


                postRepository.save(post);
                userRepository.save(user);
            }
        }
    }
   public void unlikePost(long postId, int userId) {
       Optional<Post> postOptional = postRepository.findById(postId);
       Optional<User> userOptional = userRepository.findById(userId);

       if (postOptional.isPresent() && userOptional.isPresent()) {
           Post post = postOptional.get();
           User user = userOptional.get();

           if (post.getLikedUsers().contains(user)) {
               post.getLikedUsers().remove(user);
               user.getLikedPosts().remove(post);
               post.setLikes(post.getLikes() - 1);
               postRepository.save(post);
               userRepository.save(user);
           }
       }
   }
   public List<Post> getPosts (Integer userId){
        User user = userRepository.findById(userId).get();
        return user.getPosts();
   }


}