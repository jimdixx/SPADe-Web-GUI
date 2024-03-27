package cz.zcu.fav.kiv.antipatterndetectionapp.controller.detecting;

import cz.zcu.fav.kiv.antipatterndetectionapp.service.detecting.DetectingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("v2/detecting")
public class DetectingController {

    @Autowired
    private DetectingService detectingService;

    @Value("${detecting.service.url}")
    private String serviceUrl;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> handleRequest(HttpServletRequest request, @RequestBody(required = false) String requestBody, HttpMethod method) {
        try {
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String url = serviceUrl + path.substring("/v2/detecting".length());

            ResponseEntity<String> response = detectingService.exchangeWithExternalService(url, requestBody, method);

            return response;
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
