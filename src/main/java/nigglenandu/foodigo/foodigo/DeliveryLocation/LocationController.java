package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class LocationController {
    @Autowired
    private IDeliveryLocationService locationService;

    @PostMapping("/update")
    private ResponseEntity<?> updateLocation(@RequestBody LocationDto locationDto){
        locationService.updateLocation(locationDto);
        return ResponseEntity.ok("Successfully updated location");
    }

    @GetMapping("/{deliveryId}")
    private ResponseEntity<LocationDto> getLocation(@PathVariable Long deliveryId){
        return ResponseEntity.ok( locationService.getLocation(deliveryId));
    }
}
