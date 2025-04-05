package nigglenandu.foodigo.foodigo.Security.Authentication;

import nigglenandu.foodigo.foodigo.Security.Paylaod.JwtResponse;
import nigglenandu.foodigo.foodigo.Security.Paylaod.LoginRequest;
import nigglenandu.foodigo.foodigo.Security.Paylaod.SignupRequest;

public interface IServiceAuth {
    JwtResponse authentication(LoginRequest loginRequest);
    String registerUser(SignupRequest signupRequest);
    String logoutUser();
}
