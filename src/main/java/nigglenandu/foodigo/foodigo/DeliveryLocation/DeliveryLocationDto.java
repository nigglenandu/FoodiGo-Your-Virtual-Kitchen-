package nigglenandu.foodigo.foodigo.DeliveryLocation;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryLocationDto {
    private Long deliveryId;
    private double Longitude;
    private double latitude;
    private long timeStamp;
}
