package PWC.controllers;

import PWC.entities.Commentaire;
import PWC.services.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentaireController {
    @Autowired
    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @PostMapping("/post/{postId}/user/{userId}")
    public Commentaire createComment(@PathVariable Long postId, @PathVariable Integer userId, @RequestBody Commentaire commentaire) {
        return commentaireService.createComment(postId, userId, commentaire);
    }

    @GetMapping("/post/{postId}")
    public List<Commentaire> getCommentsByPost(@PathVariable Long postId) {
        return commentaireService.getCommentsByPost(postId);
    }

    @PutMapping("/{commentId}")
    public Commentaire updateComment(@PathVariable Long commentId, @RequestBody String newContent) {
        return commentaireService.updateComment(commentId, newContent);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentaireService.deleteComment(commentId);
    }
}
