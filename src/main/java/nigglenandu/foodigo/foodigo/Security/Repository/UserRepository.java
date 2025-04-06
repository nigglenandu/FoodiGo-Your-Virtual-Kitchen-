package nigglenandu.foodigo.foodigo.Security.Repository;

import nigglenandu.foodigo.foodigo.Security.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Long> {

    Optional<UserApp> findByUsername(String username);
}
