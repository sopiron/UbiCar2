package com.uade.tpo.demo.service.location;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    @Override
    public double[] getCoordinatesFromAddress(String address, String zone) {
        try {
            String fullAddress = buildFullAddress(address, zone);

            System.out.println("Buscando dirección en Nominatim: " + fullAddress);

            String url = UriComponentsBuilder
                    .fromHttpUrl("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", fullAddress)
                    .queryParam("format", "json")
                    .queryParam("limit", 1)
                    .toUriString();

            System.out.println("URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "UbiCar-TPO/1.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Object[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object[].class
            );

            Object[] results = response.getBody();

            System.out.println("Respuesta Nominatim: " + results);

            if (results == null || results.length == 0) {
                System.out.println("No encontró resultados. Devuelve coordenadas por zona.");
                return defaultCoordinatesByZone(zone);
            }

            Map<String, Object> firstResult = (Map<String, Object>) results[0];

            String latString = firstResult.get("lat").toString();
            String lonString = firstResult.get("lon").toString();

            double latitude = Double.parseDouble(latString);
            double longitude = Double.parseDouble(lonString);

            System.out.println("LATITUD FINAL: " + latitude);
            System.out.println("LONGITUD FINAL: " + longitude);

            return new double[] { latitude, longitude };

        } catch (Exception e) {
            System.out.println("ERROR REAL EN NOMINATIM:");
            e.printStackTrace();
             return defaultCoordinatesByZone(zone);
        }
    }

    private String buildFullAddress(String address, String zone) {
        String safeAddress = address != null ? address : "";

        safeAddress = safeAddress
                .replace("Av.", "Avenida")
                .replace("av.", "Avenida")
                .trim();

        return safeAddress + ", CABA, Argentina";
    }

    private double[] defaultCoordinatesByZone(String zone) {
        if (zone == null) {
        return new double[] { -34.6037, -58.3816 };
    }

    switch (zone.toUpperCase()) {
        case "PALERMO":
            return new double[] { -34.5889, -58.4306 };
        case "RECOLETA":
            return new double[] { -34.5895, -58.3974 };
        case "BELGRANO":
            return new double[] { -34.5627, -58.4583 };
        default:
            return new double[] { -34.6037, -58.3816 };
    }
    }
}