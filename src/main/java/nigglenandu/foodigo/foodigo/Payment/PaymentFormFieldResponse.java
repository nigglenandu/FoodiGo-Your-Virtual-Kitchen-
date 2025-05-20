package nigglenandu.foodigo.foodigo.Payment;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PaymentFormFieldResponse {
    private String esewaUrl;
    private Map<String, String> formField = new HashMap<>();
}
