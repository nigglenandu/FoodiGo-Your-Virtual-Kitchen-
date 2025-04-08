package nigglenandu.foodigo.foodigo.DeliveryLocation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class DeliveryLocation {
    @Id
    private Long deliveryId;
    private double Longitude;
    private double latitude;
}
