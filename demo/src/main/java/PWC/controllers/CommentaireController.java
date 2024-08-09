package PWC.controllers;

import PWC.entities.Commentaire;
import PWC.services.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/comments")
public class CommentaireController {

    @Autowired
    private final CommentaireService commentaireService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @PostMapping("/post/{postId}/user/{userId}")
    public Commentaire createComment(@PathVariable Long postId, @PathVariable Integer userId, @RequestBody Commentaire commentaire) {
        Commentaire c = commentaireService.createComment(postId, userId, commentaire);
        sendNotification(c);
        return c;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        emitters.add(emitter);

        return emitter;
    }

    private void sendNotification(Commentaire comment) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("comment").data(comment));
            } catch (IOException e) {
                deadEmitters.add(emitter);
                // Log the error to understand what went wrong
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        }
        emitters.removeAll(deadEmitters);
    }
}
