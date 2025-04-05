package nigglenandu.foodigo.foodigo.Security.Authentication;

import nigglenandu.foodigo.foodigo.Security.Paylaod.JwtResponse;
import nigglenandu.foodigo.foodigo.Security.Paylaod.LoginRequest;
import nigglenandu.foodigo.foodigo.Security.Paylaod.SignupRequest;
import nigglenandu.foodigo.foodigo.Security.Repository.RoleRepository;
import nigglenandu.foodigo.foodigo.Security.Repository.UserRepository;
import nigglenandu.foodigo.foodigo.Security.SecurityUtils.JwtUtils;
import nigglenandu.foodigo.foodigo.Security.model.RoleEntity;
import nigglenandu.foodigo.foodigo.Security.model.Roles;
import nigglenandu.foodigo.foodigo.Security.model.UserApp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IServiceAuth{
    private final RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public JwtResponse authentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFormUsername(userDetails);

        Optional<UserApp> userAppOptional = userRepository.findUsername(userDetails.getUsername());

        if(userAppOptional.isEmpty()){
            throw new RuntimeException("Error: User not found!");
        }

        UserApp user = userAppOptional.get();
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, roles, user.getId(), user.getUsername(), user.getEmail());
    }



    @Override
    public String registerUser(SignupRequest signupRequest) {
        if(userRepository.findByUsername(signupRequest.getUsername()).isPresent()){
            return "Error: Username is already taken!";
        }

        UserApp user = new UserApp();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhoneNumber());

        RoleEntity role = roleRepository.findByRole(Roles.CUSTOMER)
                        .orElseThrow(() -> new RuntimeException("Error: Defaullt role (ROLE_USER) not found!"));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public String logoutUser() {
        return "";
    }
}
