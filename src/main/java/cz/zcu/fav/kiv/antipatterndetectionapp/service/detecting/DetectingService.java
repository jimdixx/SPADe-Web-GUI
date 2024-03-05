package cz.zcu.fav.kiv.antipatterndetectionapp.service.detecting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class DetectingService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> exchangeWithExternalService(String url, String requestBody, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.exchange(url, method, requestEntity, String.class);
        } catch (RestClientResponseException e) {
                return ResponseEntity.status(e.getRawStatusCode()).body(e.getStatusText());
        }
    }
}
