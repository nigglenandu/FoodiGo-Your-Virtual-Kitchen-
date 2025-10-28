package nigglenandu.foodigo.foodigo.Chef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChefDishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ChefRepository chefRepository;

    public DishEntity postDish(Long chefId, DishEntity dish) {
        ChefEntity chef = chefRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef not found"));
        dish.setChef(chef);
        return dishRepository.save(dish);
    }

    public List<DishEntity> getDishesByChef(Long chefId) {
        return dishRepository.findByChef_ChefId(chefId);
    }

    public void deleteDish(Long dishId) {
        dishRepository.deleteById(dishId);
    }

    public DishEntity updateDish(Long dishId, DishEntity updatedDish) {
        DishEntity dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        dish.setDishName(updatedDish.getDishName());
        dish.setDescription(updatedDish.getDescription());
        dish.setPrice(updatedDish.getPrice());
        dish.setImageUrl(updatedDish.getImageUrl());
        return dishRepository.save(dish);
    }
}
