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

    public List<DishEntity> getChefDishes(Long chefId) {
        return dishRepository.findByChefChefId(chefId);
    }
}
