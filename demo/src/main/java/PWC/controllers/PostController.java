package PWC.controllers;

import PWC.entities.Notifications;
import PWC.entities.Post;

import PWC.entities.User;
import PWC.repository.CommentaireRepository;
import PWC.repository.NotificationRepository;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import PWC.services.NotificationService;
import PWC.services.PostService;
import PWC.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public PostController(PostRepository postRepository, SimpMessagingTemplate messagingTemplate) {
        this.postRepository = postRepository;
        //this.messagingTemplate = messagingTemplate;
    }
    @Autowired
    private PostService postService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("email") String currentEmail) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = userRepository.findByEmail(currentEmail);

            Post post = new Post();
            post.setUser(authenticatedUser);

            if (content != null && !content.isEmpty()) {
                post.setContent(content);
            }

            if (image != null && !image.isEmpty()) {
                post.setImage(image.getBytes());
            }

            post.setCommentaires(0);
            post.setLikes(0);

            if (content != null || image != null) {
                Post savedPost = postRepository.save(post);

                /*List<User> allUsersExceptCurrentUser = new ArrayList<>();
                for (User u : userRepository.findAll()) {
                    if (u.getId() != authenticatedUser.getId()) {
                        allUsersExceptCurrentUser.add(u);
                    }
                }
                for (User user : allUsersExceptCurrentUser) {
                    Notifications notification = new Notifications();
                    notification.setMessage("New post added: " + post.getContent());
                    notification.setPostId(savedPost.getId());
                    notification.setSenderId(authenticatedUser.getId());
                    notification.setReceiverId(user.getId());
                    notification.increment();
                    notificationService.createNotification(notification);
                    messagingTemplate.convertAndSendToUser(user.getEmail(), "/topic/notifications", notification);
                    System.out.println(notification);
                    System.out.println(notification.getMessage());
                }*/
                return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }
    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId, @RequestParam Integer userId) {
        postService.likePost(postId, userId);
    }
    @GetMapping("/getpostsbyuser")
    public List<Post> getpostsbyUser (@RequestParam Integer userId) {
        return postService.getPosts(userId);
    }
    @GetMapping("/getpostsbyuser/{userId}")
    public List<Post> getpostsbyUserr (@PathVariable Integer userId) {
        return postRepository.findByUserId(userId);}

        @PostMapping("/{postId}/unlike")
    public void unlikePost(@PathVariable Long postId, @RequestParam Integer userId) {
        postService.unlikePost(postId, userId);
    }
    @GetMapping("/parId/{postId}")
    public Post getPostParId(@PathVariable Long postId) {
        return postRepository.findById(postId).get();
    }

}
