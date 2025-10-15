package nigglenandu.foodigo.foodigo.DeliveryLocation;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryLocationDto {
    private Long id;
    private Long deliveryId;
    private double longitude;
    private double latitude;
    private long timestamp;
}
