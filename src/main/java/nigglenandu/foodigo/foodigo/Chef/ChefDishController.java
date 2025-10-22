package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chef")
public class ChefDishController {

    @Autowired
    private ChefDishService chefDishService;

    @PostMapping("/{chefId}/dishes")
    public ResponseEntity<DishEntity> addDish(@PathVariable Long chefId, @RequestBody DishEntity dish) {
        return ResponseEntity.ok(chefDishService.postDish(chefId, dish));
    }

    @GetMapping("/{chefId}/dishes")
    public ResponseEntity<List<DishEntity>> getAllDishes(@PathVariable Long chefId) {
        return ResponseEntity.ok(chefDishService.getChefDishes(chefId));
    }
}
