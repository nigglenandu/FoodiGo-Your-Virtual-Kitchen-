package nigglenandu.foodigo.foodigo.Security.Repository;

import nigglenandu.foodigo.foodigo.Security.model.RoleEntity;
import nigglenandu.foodigo.foodigo.Security.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(Roles role);
}
