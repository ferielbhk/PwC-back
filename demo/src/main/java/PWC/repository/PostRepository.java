// PostRepository.java
package PWC.repository;

import PWC.entities.Post;
import PWC.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Integer userId);
}
