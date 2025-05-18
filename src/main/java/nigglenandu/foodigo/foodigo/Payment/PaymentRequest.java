package nigglenandu.foodigo.foodigo.Payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private double amount;
    private double taxAmount;
    private double serviceCharge;
    private double deliveryCharge;
    private String productCode;
}
