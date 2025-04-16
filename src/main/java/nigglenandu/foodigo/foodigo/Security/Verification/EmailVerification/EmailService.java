package nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailVerification(String to, String verificationUrl){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Verify your email foodigo");
        message.setText("Click the link to verify your account: " + verificationUrl);
    }
}
