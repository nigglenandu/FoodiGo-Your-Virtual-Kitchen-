package nigglenandu.foodigo.foodigo.Chef;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ChefFinalOrderSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String grandTotalPrice;
    private String mobileNumber;
    private String name;
    private String randomUID;
    private String status;

    @OneToMany(mappedBy = "summary", cascade = CascadeType.ALL)
    private List<ChefFinalOrder> dishOrders;

    public ChefFinalOrderSummary() {}

    public ChefFinalOrderSummary(String address, String grandTotalPrice, String mobileNumber,
                                 String name, String randomUID, String status) {
        this.address = address;
        this.grandTotalPrice = grandTotalPrice;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.randomUID = randomUID;
        this.status = status;
    }

}
