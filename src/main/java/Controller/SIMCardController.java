package Controller;

import Entity.SimCardTransaction;
import Entity.SimCardTransactionRepo;
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
    private final SimCardTransactionRepo repo;

    public SIMCardController(RestTemplate restTemplate, SimCardTransactionRepo repo) {
        this.restTemplate = restTemplate;
        this.repo = repo;
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateSIMCard(@RequestBody SIMCardRequest request) {
        String actuatorUrl = "http://localhost:8444/actuate";
        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(actuatorUrl, new ActuatorRequest(request.getICCID()), ActuatorResponse.class);

        boolean success = response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess();
        repo.save(new SimCardTransaction(request.getICCID(), request.getCustomerEmail(), success));

        if (success) {
            return ResponseEntity.ok("SIM card activation successful.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SIM card activation failed.");
        }
    }

    public static class SIMCardRequest {
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

    public static class ActuatorRequest {
        private String iccid;

        public ActuatorRequest(String iccid) {
            this.iccid = iccid;
        }

        // Getter and setter
        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }
    }
}
