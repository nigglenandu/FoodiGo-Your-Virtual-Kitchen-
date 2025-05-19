//package nigglenandu.foodigo.foodigo.Payment;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class EsewaPaymentService {
//
//    private static final Logger logger = LoggerFactory.getLogger(EsewaPaymentService.class);
//
//    @Value("${esewa.test.url}")
//    private String esewaUrl;
//
//    @Value("${esewa.merchant.id}")
//    private String merchantId;
//
//    @Value("${esewa.success.url}")
//    private String successUrl;
//
//    @Value("${esewa.failure.url}")
//    private String failureUrl;
//
//    public PaymentFormFieldResponse initiatePayment(PaymentRequest request) {
//        String transactionUuid = UUID.randomUUID().toString();
//        double totalAmount = request.getAmount() + request.getTaxAmount() +
//                request.getServiceCharge() + request.getDeliveryCharge();
//
//        Map<String, String> formFields = new HashMap<>();
////        formFields.put("amt", String.valueOf(request.getAmount()));
////        formFields.put("txAmt", String.valueOf(request.getTaxAmount()));
////        formFields.put("psc", String.valueOf(request.getServiceCharge()));
////        formFields.put("pdc", String.valueOf(request.getDeliveryCharge()));
////        formFields.put("tAmt", String.valueOf(totalAmount));
////        formFields.put("pid", transactionUuid);
////        formFields.put("scd", merchantId);
////        formFields.put("su", successUrl + "?transaction_uuid=" + transactionUuid);
////        formFields.put("fu", failureUrl);
//        formFields.put("amount", String.format("%.2f", request.getAmount()));
//        formFields.put("tax_amount", String.format("%.2f", request.getTaxAmount()));
//        formFields.put("product_service_charge", String.format("%.2f", request.getServiceCharge()));
//        formFields.put("product_delivery_charge", String.format("%.2f", request.getDeliveryCharge()));
//        formFields.put("total_amount", String.format("%.2f", totalAmount));
//        formFields.put("transaction_uuid", transactionUuid);
//        formFields.put("product_code", merchantId);
//        formFields.put("success_url", successUrl + "?transaction_uuid=" + transactionUuid);
//        formFields.put("failure_url", failureUrl);
//        formFields.put("signed_field_names", "total_amount,transaction_uuid,product_code");
//
//        String data = "total_amount=" + String.format("%.2f", totalAmount) +
//                ",transaction_uuid=" + transactionUuid +
//                ",product_code=" + merchantId;
//        try {
//            String signature = generateSignature(data, "8gBm/:&EnhH.1/q");
//            formFields.put("signature", signature);
//        } catch (Exception e) {
//            logger.error("Signature generation failed: {}", e.getMessage());
//            throw new RuntimeException("Failed to generate signature");
//        }
//
//        PaymentFormFieldResponse response = new PaymentFormFieldResponse();
//        response.setEsewaUrl(esewaUrl);
//        response.setFormField(formFields);
//
//        return response;
//    }
//
//    private String generateSignature(String data, String secretKey) throws Exception {
//        Mac mac = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
//        mac.init(secretKeySpec);
//        byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
//        return Base64.getEncoder().encodeToString(hash);
//    }
//
//    public boolean verifyPayment(String transactionUuid, String refId, double totalAmount) {
//        String verificationUrl = "https://uat.esewa.com.np/epay/transrec";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("amt", String.valueOf(totalAmount));
//        map.add("pid", transactionUuid);
//        map.add("rid", refId);
//        map.add("scd", merchantId);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(verificationUrl, request, String.class);
//
//        return response.getBody() != null && response.getBody().contains("<response_code>Success</response_code>");
//    }
//}


package nigglenandu.foodigo.foodigo.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EsewaPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(EsewaPaymentService.class);

    @Value("${esewa.test.url}")
    private String esewaUrl;

    @Value("${esewa.merchant.id}")
    private String merchantId;

    @Value("${esewa.success.url}")
    private String successUrl;

    @Value("${esewa.failure.url}")
    private String failureUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EsewaPaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public PaymentFormFieldResponse initiatePayment(PaymentRequest request) {
        // Validate input parameters
        validatePaymentRequest(request);

        String transactionUuid = UUID.randomUUID().toString();
        double totalAmount = calculateTotalAmount(request);

        Map<String, String> formFields = new HashMap<>();
        formFields.put("amount", String.format("%.2f", request.getAmount()));
        formFields.put("tax_amount", String.format("%.2f", request.getTaxAmount()));
        formFields.put("product_service_charge", String.format("%.2f", request.getServiceCharge()));
        formFields.put("product_delivery_charge", String.format("%.2f", request.getDeliveryCharge()));
        formFields.put("total_amount", String.format("%.2f", totalAmount));
        formFields.put("transaction_uuid", transactionUuid);
        formFields.put("product_code", merchantId);
        formFields.put("success_url", successUrl + "?transaction_uuid=" + transactionUuid);
        formFields.put("failure_url", failureUrl);
        formFields.put("signed_field_names", "total_amount,transaction_uuid,product_code");

        String signatureData = buildSignatureData(totalAmount, transactionUuid);
        String signature = generateSignature(signatureData, "8gBm/:&EnhH.1/q");
        formFields.put("signature", signature);

        logger.info("Payment initiated successfully: transaction_uuid={}", transactionUuid);

        PaymentFormFieldResponse response = new PaymentFormFieldResponse();
        response.setEsewaUrl(esewaUrl);
        response.setFormField(formFields);
        return response;
    }

    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getAmount() < 0 || request.getTaxAmount() < 0 ||
                request.getServiceCharge() < 0 || request.getDeliveryCharge() < 0) {
            throw new IllegalArgumentException("All payment amounts must be non-negative");
        }
    }

    private double calculateTotalAmount(PaymentRequest request) {
        return request.getAmount() + request.getTaxAmount() +
                request.getServiceCharge() + request.getDeliveryCharge();
    }

    private String buildSignatureData(double totalAmount, String transactionUuid) {
        return "total_amount=" + String.format("%.2f", totalAmount) +
                ",transaction_uuid=" + transactionUuid +
                ",product_code=" + merchantId;
    }

    private String generateSignature(String data, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            logger.error("Failed to generate signature: {}", e.getMessage());
            throw new RuntimeException("Signature generation failed: " + e.getMessage(), e);
        }
    }

    public boolean verifyPayment(String transactionUuid, String refId, double totalAmount) {
        String verificationUrl = String.format(
                "https://rc-epay.esewa.com.np/api/epay/transaction/status?product_code=%s&total_amount=%.2f&transaction_uuid=%s",
                merchantId, totalAmount, transactionUuid
        );

        logger.info("Verifying payment: transaction_uuid={}, refId={}", transactionUuid, refId);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(verificationUrl, String.class);
            Map<String, String> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            boolean isVerified = "SUCCESS".equals(responseMap.get("status"));
            logger.info("Payment verification result: transaction_uuid={}, verified={}", transactionUuid, isVerified);
            return isVerified;
        } catch (Exception e) {
            logger.error("Payment verification failed for transaction_uuid={}: {}", transactionUuid, e.getMessage());
            return false;
        }
    }
}