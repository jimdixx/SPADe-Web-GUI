package cz.zcu.fav.kiv.antipatterndetectionapp.controller.detecting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DetectingControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${detecting.service.url}")
    private String serviceUrl;

    @Test
    public void testExternalServiceAvailabilityOnMetrics() {
        ResponseEntity<String> response = restTemplate.getForEntity(serviceUrl + "/metrics", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testExternalServiceAvailabilityOnIndicators() {
        ResponseEntity<String> response = restTemplate.getForEntity(serviceUrl + "/indicators", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testExternalServiceAvailabilityOnNonExistEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(serviceUrl + "/nonexistingendpoint", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}




