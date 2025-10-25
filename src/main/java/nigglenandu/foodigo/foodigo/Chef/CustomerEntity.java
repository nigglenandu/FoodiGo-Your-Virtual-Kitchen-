package nigglenandu.foodigo.foodigo.Chef;

package nigglenandu.foodigo.foodigo.Customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String name;
    private String email;
    private String mobile;
    private String address;
}
