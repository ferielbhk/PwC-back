package PWC.repository;

import PWC.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
   // Query("SELECT u FROM User u WHERE u.email = :email")



    User findByEmail(String userEmail);



}
