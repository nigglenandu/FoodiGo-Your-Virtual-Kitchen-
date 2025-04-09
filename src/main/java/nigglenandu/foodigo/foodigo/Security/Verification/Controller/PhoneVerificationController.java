package nigglenandu.foodigo.foodigo.Security.Verification.Controller;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import nigglenandu.foodigo.foodigo.Security.Repository.UserRepository;
import nigglenandu.foodigo.foodigo.Security.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("api/phone")
public class PhoneVerificationController {

    @Autowired
    private UserRepository userRepository;

    private final Map<String, String> storedOtp = new ConcurrentHashMap<>();

    @PostMapping("/sendOtp")
    public ResponseEntity<String>  optSend(@RequestParam String phoneNumber){
        String otp = generateOtp();
        storedOtp.put(phoneNumber, otp);

        Message.creator(
                new PhoneNumber("MyNum"),
                new PhoneNumber("TwilioNum"),
                "Your OTP: " + otp
        ).create();

        System.out.println("OTP for " + phoneNumber + ": " + otp);
        return ResponseEntity.ok("OTP sent");
    }

    public String generateOtp(){
        return String.valueOf((int)((Math.random() * 9000) + 1000));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String>  verifyOtp(@RequestParam String phoneNumber,
                                             @RequestParam String otp){

        if (storedOtp.containsKey(phoneNumber) && storedOtp.get(phoneNumber).equals(otp)){
            Optional<UserApp> userAppOptional = userRepository.findByPhoneNumber(phoneNumber);
            if(userAppOptional.isPresent()){
                UserApp userApp = userAppOptional.get();
                userApp.setPhoneVerified(true);
                userRepository.save(userApp);
                storedOtp.remove(phoneNumber);
                System.out.println("Your OTP has been verified.");
                return ResponseEntity.ok("OTP verified");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }
}