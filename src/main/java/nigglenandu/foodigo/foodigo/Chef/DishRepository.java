package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
    List<DishEntity> findByChefChefId(Long chefId);

    List<DishEntity> findByChef_ChefId(Long chefId);
}
