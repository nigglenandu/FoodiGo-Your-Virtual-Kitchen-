package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryLocationImpl implements IDeliveryLocationService{
    @Autowired
    private DeliveryLocationRepository deliveryLocationRepository;

    @Override
    public void updateLocation(DeliveryLocationDto locationDto) {
        DeliveryLocation location = new DeliveryLocation();
        location.setDeliveryId(locationDto.getDeliveryId());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());

        deliveryLocationRepository.save(location);
    }

    @Override
    public DeliveryLocationDto getCurrentLocation(Long deliveryId) {
        DeliveryLocation location = deliveryLocationRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery person not found"));
        DeliveryLocationDto dto = new DeliveryLocationDto();
        dto.setDeliveryId(location.getDeliveryId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());

        return dto;
    }

    @Override
    public List<DeliveryLocationDto> getLocationHistory(Long deliveryId) {
        List<DeliveryLocation> history = deliveryLocationRepository.findByDeliveryIdOrderByTimestamp(deliveryId);

        return history.stream()
                .map(loc -> new DeliveryLocationDto(loc.getDeliveryId(), loc.getLatitude(), loc.getLongitude(), loc.getTimestamp()))
                .collect(Collectors.toList());
    }
}
