package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryLocationImpl implements IDeliveryLocationService{
    @Autowired
    private DeliveryLocationRepository deliveryLocationRepository;

    @Override
    public void updateLocation(LocationDto locationDto) {
        DeliveryLocation location = new DeliveryLocation();
        location.setDeliveryId(locationDto.getDeliveryId());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());

        deliveryLocationRepository.save(location);
    }

    @Override
    public LocationDto getLocation(Long deliveryId) {
        DeliveryLocation location = deliveryLocationRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery person not found"));
        LocationDto dto = new LocationDto();
        dto.setDeliveryId(location.getDeliveryId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());

        return dto;
    }
}
