package nigglenandu.foodigo.foodigo.DeliveryLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
                .map(loc -> new DeliveryLocationDto(loc.getId(), loc.getDeliveryId(), loc.getLatitude(), loc.getLongitude(), loc.getTimestamp()))
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    @Override
    public double calculateShortestPathDistance(Long deliveryId) {
        List<DeliveryLocation> history = deliveryLocationRepository.findByDeliveryIdOrderByTimestamp(deliveryId);
        int n = history.size();

        if (n <= 1) return 0.0;

        double[][] graph = new double[n][n];

        for (int i = 0; i < n; i++) {
            DeliveryLocation from = history.get(i);
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                DeliveryLocation to = history.get(j);
                graph[i][j] = distance(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());
            }
        }


        double[] dist = new double[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Double.MAX_VALUE);
        dist[0] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = -1;


            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                    u = j;
                }
            }

            visited[u] = true;


            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] > 0) {
                    dist[v] = Math.min(dist[v], dist[u] + graph[u][v]);
                }
            }
        }

        return dist[n - 1];
    }



}
