package nigglenandu.foodigo.foodigo.DeliveryLocation;

import java.util.List;

public interface IDeliveryLocationService {
    void updateLocation(DeliveryLocationDto locationDto);
    DeliveryLocationDto getCurrentLocation(Long deliveryId);
    List<DeliveryLocationDto> getLocationHistory(Long deliveryId);
}
