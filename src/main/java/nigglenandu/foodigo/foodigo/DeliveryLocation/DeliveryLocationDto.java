package nigglenandu.foodigo.foodigo.DeliveryLocation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryLocationDto {
    private Long deliveryId;
    private double Longitude;
    private double latitude;
    private long timeStamp;
}
