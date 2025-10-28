package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chef")
public class ChefDishController {

    @Autowired
    private ChefDishService dishService;

    @PostMapping("/{chefId}/dish")
    public ResponseEntity<DishEntity> postDish(@PathVariable Long chefId, @RequestBody DishEntity dish) {
        return ResponseEntity.ok(dishService.postDish(chefId, dish));
    }

    @GetMapping("/{chefId}/dishes")
    public ResponseEntity<List<DishEntity>> getChefDishes(@PathVariable Long chefId) {
        return ResponseEntity.ok(dishService.getDishesByChef(chefId));
    }

    @PutMapping("/dish/{dishId}")
    public ResponseEntity<DishEntity> updateDish(@PathVariable Long dishId, @RequestBody DishEntity dish) {
        return ResponseEntity.ok(dishService.updateDish(dishId, dish));
    }

    @DeleteMapping("/dish/{dishId}")
    public ResponseEntity<String> deleteDish(@PathVariable Long dishId) {
        dishService.deleteDish(dishId);
        return ResponseEntity.ok("Dish deleted successfully");
    }
}
