package PWC.services;
import PWC.entities.Story;
import PWC.entities.User;
import PWC.repository.StoryRepository; // Assume you have a StoryRepository
import PWC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class StoryService {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository; // Assume you have a StoryRepository

    @Autowired
    public StoryService(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    public Story addStory(MultipartFile image, String currentEmail) {
        try {
            User authenticatedUser = userRepository.findByEmail(currentEmail);
            if (authenticatedUser == null) {
                throw new RuntimeException("User not found");
            }

            Story story = new Story();
            story.setUser(authenticatedUser);
            story.setDateAjout(new Date());
            story.setImage(image.getBytes());

            return storyRepository.save(story);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process image", e);
        }
    }
}
