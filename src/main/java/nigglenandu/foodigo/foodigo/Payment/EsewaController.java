package nigglenandu.foodigo.foodigo.Payment;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:63342")
public class EsewaController {

    private static final Logger logger = LoggerFactory.getLogger(EsewaController.class);

    private final EsewaPaymentService paymentService;
    private final ObjectMapper objectMapper;

    public EsewaController(ObjectMapper objectMapper, EsewaPaymentService paymentService) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentFormFieldResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        logger.info("Initiating payment: amount={}, productCode={}", request.getAmount(), request.getProductCode());
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestParam("transaction_uuid") String transactionUuid,
            @RequestParam("data") String data
    ) {
        logger.info("Received payment verification request: transaction_uuid={}", transactionUuid);
        try {
            String decodedData = new String(Base64.getDecoder().decode(data));
            JsonNode jsonNode = objectMapper.readTree(decodedData);

            String refId = jsonNode.path("transaction_code").asText();
            double totalAmount = jsonNode.path("totalAmount").asDouble();

            String signedFieldNames = jsonNode.path("signed_field_names").asText();
            String signature = jsonNode.path("signature").asText();
            if(!verifySignature(jsonNode, signedFieldNames, signature)){
                logger.warn("Signature verification failed for transaction_uuid={}", transactionUuid);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
            }

            boolean verified = paymentService.verifyPayment(transactionUuid, refId, totalAmount);
            String responseMessage = verified ? "Payment Verified" : "Payment Verification Failed";
            logger.info("verification result for transaction_uuid={}: {}", transactionUuid, responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e){
            logger.error("Failed to process verification request for transaction_uuid={}: {}", transactionUuid, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data format: " + e.getMessage());
        }
    }

    private boolean verifySignature(JsonNode jsonNode, String signedFieldNames, String receivedSignature){
        try{
            String[] fields = signedFieldNames.split(",");
            StringBuilder data = new StringBuilder();
            for(String field : fields){
                data.append(field).append("=").append(jsonNode.path(field).asText()).append(",");
            }
            data.setLength(data.length() - 1);

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec("8gBm/:&EnhH.1/q".getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.toString().getBytes("UTF-8"));
            String calculatedSignature = Base64.getEncoder().encodeToString(hash);

            return calculatedSignature.equals(receivedSignature);
        } catch (Exception e){
            logger.error("Signature verification failed: {}", e.getMessage());
            return false;
        }
    }

    @GetMapping("/failure")
    public ResponseEntity<String> handlePaymentFailure() {
        logger.info("Payment failed, redirecting to failure endpoint");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        logger.warn("Validation error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e){
        logger.error("Unexpected error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}
