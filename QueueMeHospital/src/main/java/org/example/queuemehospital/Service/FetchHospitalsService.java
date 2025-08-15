package org.example.queuemehospital.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Hospital;
import org.example.queuemehospital.Repository.HospitalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchHospitalsService {

    private final HospitalRepository hospitalRepository;

    public List<Hospital> fetchAndSaveHospitalsFromOSM(double lat, double lon, double radiusKm) {
        List<Hospital> hospitals = new ArrayList<>();
        double latDegree = radiusKm / 111.0;
        double lonDegree = radiusKm / (111.0 * Math.cos(Math.toRadians(lat)));

        double minLat = lat - latDegree;
        double maxLat = lat + latDegree;
        double minLon = lon - lonDegree;
        double maxLon = lon + lonDegree;

        String url = String.format(
                "https://nominatim.openstreetmap.org/search?format=json&amenity=hospital&limit=20&bounded=1&viewbox=%f,%f,%f,%f",
                minLon, minLat, maxLon, maxLat);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode nodes = mapper.readTree(response);

            for (JsonNode node : nodes) {
                String name = node.has("display_name") ? node.get("display_name").asText() : "Unknown Hospital";
                double latitude = node.has("lat") ? node.get("lat").asDouble() : 0;
                double longitude = node.has("lon") ? node.get("lon").asDouble() : 0;

                // Avoid duplicate by name + lat + lon
                Hospital existing = hospitalRepository.findHospitalByName(name);
                if (existing == null) {
                    Hospital hospital = new Hospital(null, name, latitude, longitude);
                    hospitals.add(hospital);
                }
            }
            return hospitals;
        } catch (Exception e) {
            throw new ApiException("Failed to fetch or save hospitals from OpenStreetMap");
        }
    }
}
