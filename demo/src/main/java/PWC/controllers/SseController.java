package PWC.controllers;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("api/v1/sse")
public class SseController {
/*
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private Long lastId = 0L;

    @GetMapping("")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(60000L); // 1 minute timeout
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("message")
                    .id("" + lastId++)
                    .data("connexion"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        sendNotification("heartbeat");
    }

    public void sendNotification(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .id("" + ++lastId)
                        .data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
    public void sendCustomNotification(String customMessage) {
        sendNotification(customMessage);
    }
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();





    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter();
        emitters.add(sseEmitter);

        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitters.remove(sseEmitter));

        // Optional: send a message when the connection is established
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("notification")
                    .data("Connexion établie"));
        } catch (IOException e) {
            e.printStackTrace();
            emitters.remove(sseEmitter);
        }

        return sseEmitter;
    }

*/

}