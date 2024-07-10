package PWC.controllers;

import PWC.entities.Post;

import PWC.entities.User;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import PWC.services.PostService;
import PWC.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            @RequestParam("email") String currentEmail) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("aaaaaaa"+authentication.getPrincipal().toString());
          //  User authenticatedUser = (User) authentication.getPrincipal();
            User authenticatedUser= new User();
            if(currentEmail !=null ){
                 authenticatedUser = userRepository.findByEmail((currentEmail));
                System.out.println(currentEmail);
                System.out.println(authenticatedUser);
            }

            Post post = new Post();
            post.setContent(content);
            post.setImage(image.getBytes());
            post.setCommentaires(0);
            post.setLikes(0);
            post.setUser(authenticatedUser);

            Post savedPost = postRepository.save(post);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/like/{postId}/{userId}")
    public ResponseEntity<Void> likePost(@PathVariable long postId,@PathVariable int userId) {
        postService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/{postId}/{userId}")
    public ResponseEntity<Void> unlikePost(@PathVariable long postId, @PathVariable int userId) {
        postService.unlikePost(postId, userId);
        return ResponseEntity.ok().build();
    }

}