package nigglenandu.foodigo.foodigo.Payment;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:63342")
public class EsewaController {

    private final EsewaPaymentService paymentService;

    public EsewaController(EsewaPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentFormFieldResponse> initiatePayment(@RequestBody PaymentRequest request){
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestParam("transaction_uuid") String transactionUuid,
            @RequestParam("refId") String refId,
            @RequestParam("amount") double totalAmount
    ) {
        boolean verified = paymentService.verifyPayment(transactionUuid, refId, totalAmount);
        return ResponseEntity.ok(verified ? "Payment Verified" : "Payment Verification Failed");
    }

    @GetMapping("/failure")
    public ResponseEntity<String> handlePaymentFailure() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");
    }
}
