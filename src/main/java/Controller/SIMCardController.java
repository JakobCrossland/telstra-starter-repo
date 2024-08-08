package Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SIMCardController {

    private final RestTemplate restTemplate;

    public SIMCardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateSIMCard(@RequestBody SIMCardRequest request) {
        String actuatorUrl = "http://localhost:8444/actuate";
        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(actuatorUrl, request, ActuatorResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            boolean success = response.getBody().isSuccess();

            if (success) {
                return ResponseEntity.ok("SIM card activation successful.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SIM card activation failed.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while contacting actuator.");
        }
    }

    public class SIMCardRequest {
        private String iccid;
        private String customerEmail;

        public SIMCardRequest(String iccid, String customerEmail) {
            this.iccid = iccid;
            this.customerEmail = customerEmail;
        }

        public String getICCID() {
            return iccid;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setICCID(String iccid) {
            this.iccid = iccid;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }
    }

    public class ActuatorResponse {
        private boolean success;

        public ActuatorResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
