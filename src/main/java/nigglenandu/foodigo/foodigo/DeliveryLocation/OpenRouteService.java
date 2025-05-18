package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenRouteService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openroute.api.key}")
    private String apiKey;

    public String getDirections(double originLat, double originLng, double destLat, double destLng) {
        String url = "https://api.openrouteservice.org/v2/directions/driving-car"
                + "?start=" + originLng + "," + originLat
                + "&end=" + destLng + "," + destLat;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.set("Accept", "application/geo+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}