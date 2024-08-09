package PWC.controllers;
import PWC.entities.Story;
import PWC.repository.StoryRepository;
import PWC.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
public class StoryConroller {
    @Autowired
    private StoryService storyService;
    @Autowired
    private StoryRepository storyRepository;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Story addStory(@RequestParam(value = "image") MultipartFile image,
                          @RequestParam("email") String currentEmail) {
        return storyService.addStory(image, currentEmail);
    }
    @GetMapping
    public List<Story> getStories() {
        return  storyRepository.findAll();
    }
}
