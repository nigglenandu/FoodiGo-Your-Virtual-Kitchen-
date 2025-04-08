package nigglenandu.foodigo.foodigo.Security.Verification.Configuration;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
    @PostConstruct
    public void init(){
        Twilio.init("TWILIO_SID", "TWILIO_AUTH-TOKEN");
    }
}
