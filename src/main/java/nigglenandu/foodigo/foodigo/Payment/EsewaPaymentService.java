package nigglenandu.foodigo.foodigo.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EsewaPaymentService {

    @Value("${esewa.test.url}")
    private String esewaUrl;

    @Value("${esewa.merchant.id}")
    private String merchantId;

    @Value("${esewa.success.url}")
    private String successUrl;

    @Value("${esewa.failure.url")
    private String failureUrl;

    public PaymentFormFieldResponse initiatePayment(PaymentRequest request) {
        String transactionUuid = UUID.randomUUID().toString();
        double totalAmount = request.getAmount() + request.getTaxAmount() +
                request.getServiceCharge() + request.getDeliveryCharge();

        Map<String, String> formFields = new HashMap<>();
        formFields.put("amt", String.valueOf(request.getAmount()));
        formFields.put("txAmt", String.valueOf(request.getTaxAmount()));
        formFields.put("psc", String.valueOf(request.getServiceCharge()));
        formFields.put("pdc", String.valueOf(request.getDeliveryCharge()));
        formFields.put("tAmt", String.valueOf(totalAmount));
        formFields.put("pid", transactionUuid);
        formFields.put("scd", merchantId);
        formFields.put("su", successUrl + "?transaction_uuid" + transactionUuid);
        formFields.put("fu", failureUrl);

        PaymentFormFieldResponse response = new PaymentFormFieldResponse();
        response.setEsewaUrl(esewaUrl);
        response.setFormField(formFields);

        return response;
    }

    public boolean verifyPayment(String transactionUuid, String refId, double totalAmount) {
        String verificationUrl = "https://uat.esewa.com.np/epay/transrec";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("amt", String.valueOf(totalAmount));
        map.add("pid", transactionUuid);
        map.add("rid", refId);
        map.add("scd", merchantId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(verificationUrl, request, String.class);

        return response.getBody() != null && response.getBody().contains("<response_code>Success</response_code>");
    }
}
