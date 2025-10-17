package nigglenandu.foodigo.foodigo.Chef;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChefFinalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dishId;
    private String dishName;
    private String dishPrice;
    private String dishQuantity;
    private String totalPrice;
    private String userId;
    private String randomUID;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefEntity chef;

    @ManyToOne
    @JoinColumn(name = "summary_id")
    private ChefFinalOrderSummary summary;

    public ChefFinalOrder() {}

    public ChefFinalOrder(String dishId, String dishName, String dishPrice, String dishQuantity,
                          String totalPrice, String userId, String randomUID, ChefEntity chef, ChefFinalOrderSummary summary) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishQuantity = dishQuantity;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.randomUID = randomUID;
        this.chef = chef;
        this.summary = summary;
    }

}
