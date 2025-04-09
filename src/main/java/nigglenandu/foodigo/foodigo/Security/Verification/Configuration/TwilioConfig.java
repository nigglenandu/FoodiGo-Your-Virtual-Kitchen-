package nigglenandu.foodigo.foodigo.Security.Verification.Configuration;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number}")
    private String phoneNumber;

    @PostConstruct
    public void init(){
        Twilio.init(twilioSid, twilioAuthToken);
        System.out.println("Twilio initialized successfully with phone number" + phoneNumber);
    }
}
