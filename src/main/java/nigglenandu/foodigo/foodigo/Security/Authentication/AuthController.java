package nigglenandu.foodigo.foodigo.Security.Authentication;

import jakarta.validation.Valid;
import nigglenandu.foodigo.foodigo.Security.Paylaod.JwtResponse;
import nigglenandu.foodigo.foodigo.Security.Paylaod.LoginRequest;
import nigglenandu.foodigo.foodigo.Security.Paylaod.SignupRequest;
import nigglenandu.foodigo.foodigo.Security.Repository.RoleRepository;
import nigglenandu.foodigo.foodigo.Security.Repository.UserRepository;
import nigglenandu.foodigo.foodigo.Security.SecurityUtils.JwtUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("apis/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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

        Optional<RoleEntity> userRole = roleRepository.findByRole(Roles.CUSTOMER);
        RoleEntity role = roleRepository.findByRole(Roles.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Default role not found!"));
        user.setRoles(Collections.singleton(role));


        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully!");
    }
}
