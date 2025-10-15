package nigglenandu.foodigo.foodigo.Chef;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ChefEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefId;

    private String name;
    private String kitchenName;
    private String kitchenAddress;
    private String email;
    private String phone;

    public ChefEntity() {}

    public ChefEntity(Long chefId, String name, String kitchenName, String kitchenAddress, String email, String phone) {
        this.chefId = chefId;
        this.name = name;
        this.kitchenName = kitchenName;
        this.kitchenAddress = kitchenAddress;
        this.email = email;
        this.phone = phone;
    }

}
