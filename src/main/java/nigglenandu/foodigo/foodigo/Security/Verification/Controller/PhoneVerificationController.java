package nigglenandu.foodigo.foodigo.Security.Verification.Controller;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("api/phone")
public class PhoneVerificationController {

    private final Map<String, String> storedOtp = new ConcurrentHashMap<>();

    public ResponseEntity<String>  optSend(@RequestParam String phoneNumber){
        String otp = generateOtp();
        storedOtp.put(phoneNumber, otp);

        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber("YOUR_TWILIO-PHONE"),
                "YOUR_OTP" + otp
        ).create();

        return ResponseEntity.ok("OTP sent");
    }

    public String generateOtp(){
        return String.valueOf((int)((Math.random() * 9000) + 1000));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String>  verifyOtp(@RequestParam String PhoneNumber,
                                             @RequestParam String otp){
        if (storedOtp.equals(otp)) {
            storedOtp.remove(otp);
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }
}
