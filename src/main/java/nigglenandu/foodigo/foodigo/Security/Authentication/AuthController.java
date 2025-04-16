package nigglenandu.foodigo.foodigo.Security.Authentication;

import jakarta.validation.Valid;
import nigglenandu.foodigo.foodigo.Security.Paylaod.JwtResponse;
import nigglenandu.foodigo.foodigo.Security.Paylaod.LoginRequest;
import nigglenandu.foodigo.foodigo.Security.Paylaod.SignupRequest;
import nigglenandu.foodigo.foodigo.Security.Repository.RoleRepository;
import nigglenandu.foodigo.foodigo.Security.Repository.UserRepository;
import nigglenandu.foodigo.foodigo.Security.SecurityUtils.JwtUtils;
import nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification.EmailService;
import nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification.VerificationEmailTokenRepo;
import nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification.VerificationToken;
import nigglenandu.foodigo.foodigo.Security.model.RoleEntity;
import nigglenandu.foodigo.foodigo.Security.model.Roles;
import nigglenandu.foodigo.foodigo.Security.model.UserApp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("apis/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final VerificationEmailTokenRepo verificationEmailTokenRepo;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, VerificationEmailTokenRepo verificationEmailTokenRepo, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.verificationEmailTokenRepo = verificationEmailTokenRepo;
        this.emailService = emailService;
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails);

        String jwt = jwtUtils.generateTokenFromUsername(userDetails);
        System.out.println(jwt);

        Optional<UserApp> userEntityOptional = userRepository.findByUsername(userDetails.getUsername());
        if (userEntityOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: User not found!");
        }

        UserApp user = userEntityOptional.get();

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toList());

        if(!user.isEmailVerified()){
            return ResponseEntity.badRequest().body("Please verify your email before logging in");
        }

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", roles, user.getId(), user.getUsername(), user.getEmail()));

    }

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        UserApp user = new UserApp();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setEmailVerified(false);

//        Optional<RoleEntity> userRole = roleRepository.findByRole(Roles.CUSTOMER);
        RoleEntity role = roleRepository.findByRole(Roles.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Default role not found!"));
        user.setRoles(Collections.singleton(role));


        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken vToken = new VerificationToken(token, user, LocalDateTime.now().plusMinutes(30));
        verificationEmailTokenRepo.save(vToken);

        String verificationUrl = "http://localhost:8080/apis/auth/verify?token=" + token;
        emailService.sendEmailVerification(user.getEmail(), verificationUrl);

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token){
        Optional<VerificationToken> optionalToken = verificationEmailTokenRepo.findByToken(token);

        if(optionalToken.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid token.");
        }

        VerificationToken verificationToken = optionalToken.get();

        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body("Token is expired.");
        }

        UserApp user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationEmailTokenRepo.delete(verificationToken);

        return ResponseEntity.ok("Email verified successfully!");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully! Please check your email to verify your account.");
    }
}
