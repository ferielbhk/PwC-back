package PWC.repository;

import PWC.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByUserId(Long userId);

    List<Commentaire> findByPostId(Long postId);
}
