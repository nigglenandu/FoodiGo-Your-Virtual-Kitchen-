package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chef")
public class ChefOrderController {

    @Autowired
    private ChefOrderService chefOrderService;

    @PostMapping("/{chefId}/orders")
    public ResponseEntity<ChefFinalOrderSummary> saveOrder(
            @PathVariable Long chefId,
            @RequestBody ChefFinalOrderSummary summary) {
        return ResponseEntity.ok(chefOrderService.saveOrder(chefId, summary));
    }

    @GetMapping("/{chefId}/orders")
    public ResponseEntity<List<ChefFinalOrderSummary>> getChefOrders(@PathVariable Long chefId) {
        return ResponseEntity.ok(chefOrderService.getOrdersByChef(chefId));
    }
}
