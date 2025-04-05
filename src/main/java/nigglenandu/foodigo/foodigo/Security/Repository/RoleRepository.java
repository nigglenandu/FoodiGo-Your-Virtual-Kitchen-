package nigglenandu.foodigo.foodigo.Security.Repository;

import nigglenandu.foodigo.foodigo.model.RoleEntity;
import nigglenandu.foodigo.foodigo.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(Roles role);
}
