package nigglenandu.foodigo.foodigo.DeliveryLocation;

public interface IDeliveryLocationService {
    void updateLocation(LocationDto locationDto);
    LocationDto getLocation(Long deliveryId);
}
