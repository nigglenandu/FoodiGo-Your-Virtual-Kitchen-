package nigglenandu.foodigo.foodigo.Chef;

package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChefOrderService {

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private ChefFinalOrderRepository orderRepository;

    @Autowired
    private ChefFinalOrderSummaryRepository summaryRepository;

    public ChefFinalOrderSummary saveOrder(Long chefId, ChefFinalOrderSummary summary) {
        ChefEntity chef = chefRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef not found"));
        summary.setChef(chef);

        for (ChefFinalOrder item : summary.getOrderItems()) {
            item.setChef(chef);
            item.setSummary(summary);
        }

        return summaryRepository.save(summary);
    }

    public List<ChefFinalOrderSummary> getOrdersByChef(Long chefId) {
        return summaryRepository.findByChefChefId(chefId);
    }
}
