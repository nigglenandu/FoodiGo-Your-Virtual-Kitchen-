package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "http://localhost:5173")
public class LocationController {
    @Autowired
    private IDeliveryLocationService locationService;
    @Autowired
    private OpenRouteService openRouteService;

    @PostMapping("/update")
    private ResponseEntity<?> updateLocation(@RequestBody DeliveryLocationDto locationDto){
        locationService.updateLocation(locationDto);
        return ResponseEntity.ok("Successfully updated location");
    }

    @GetMapping("/{deliveryId}")
    private ResponseEntity<DeliveryLocationDto> getLocation(@PathVariable Long deliveryId){
        return ResponseEntity.ok( locationService.getCurrentLocation(deliveryId));
    }

    @GetMapping("/{deliveryId}/history")
    public ResponseEntity<List<DeliveryLocationDto>> getLocationHistory(@PathVariable Long deliveryId){
        return ResponseEntity.ok(locationService.getLocationHistory(deliveryId));
    }

    @GetMapping("/direction")
    public ResponseEntity<String> getDirections(@RequestParam double originLat,
                                                @RequestParam double originLng,
                                                @RequestParam double destLat,
                                                @RequestParam double destLng){
        String directionsJson = openRouteService.getDirections(originLat, originLng, destLat, destLng);
        return ResponseEntity.ok().body(directionsJson);
//        return ResponseEntity.ok(openRouteService.getDirections(originLat, originLng, destLat, destLng));
    }
}
