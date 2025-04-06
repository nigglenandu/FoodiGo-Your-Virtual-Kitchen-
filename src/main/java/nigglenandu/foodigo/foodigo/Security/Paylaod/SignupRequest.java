package nigglenandu.foodigo.foodigo.Security.Paylaod;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nigglenandu.foodigo.foodigo.Security.model.Roles;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Username cannot be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Username must only contain alphanumeric characters and underscores.")
    @Size(min = 3, max = 20, message = "Minimum 3 characters are required.")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|:;,.<>?]).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\+97798\\d{7}$", message = "Invalid phone number. It must start with +97798 and be 10 digits long.")
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    private Set<String> role;
}
