package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChefFinalOrderSummaryRepository extends JpaRepository<ChefFinalOrderSummary, Long> {
    List<ChefFinalOrderSummary> findByChefChefId(Long chefId);
}