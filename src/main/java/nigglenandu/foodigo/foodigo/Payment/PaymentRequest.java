package nigglenandu.foodigo.foodigo.Payment;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
public class PaymentRequest {
    @Min(value = 0, message = "Amount must be non-negative")
    private double amount;

    @Min(value = 0, message = "Tax amount must be non-negative")
    private double taxAmount;

    @Min(value = 0, message = "Service charge must be non-negative")
    private double serviceCharge;

    @Min(value = 0, message = "Delivery charge must be non-negative")
    private double deliveryCharge;

    @NotBlank(message = "Product code must not be blank")
    private String productCode;
}