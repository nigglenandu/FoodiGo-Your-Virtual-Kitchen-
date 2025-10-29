package nigglenandu.foodigo.foodigo.Chef;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dishId;

    private String dishName;
    private String description;
    private double price;
    private boolean available;
    private String ImageUrl;

    // Link this dish to a specific Chef
    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefEntity chef;

    public DishEntity() {}

    public DishEntity(String dishName, String description, double price, boolean available, ChefEntity chef) {
        this.dishName = dishName;
        this.description = description;
        this.price = price;
        this.available = available;
        this.chef = chef;
    }
}
