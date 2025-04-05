package nigglenandu.foodigo.foodigo.Security.Authentication;

import nigglenandu.foodigo.foodigo.Security.Paylaod.JwtResponse;
import nigglenandu.foodigo.foodigo.Security.Paylaod.LoginRequest;
import nigglenandu.foodigo.foodigo.Security.Paylaod.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apis/auth")
public class AuthController {

    private final IServiceAuth serviceAuth;

    public AuthController(IServiceAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = serviceAuth.authentication(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
        try{
            String messsage = serviceAuth.registerUser(signupRequest);
            return ResponseEntity.ok(messsage);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}