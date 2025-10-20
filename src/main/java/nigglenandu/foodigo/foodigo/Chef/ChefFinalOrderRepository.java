package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChefFinalOrderRepository extends JpaRepository<ChefFinalOrder, Long> {
    List<ChefFinalOrder> findByChefChefId(Long chefId);
    List<ChefFinalOrder> findBySummarySummaryId(Long summaryId);
}