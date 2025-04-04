package nigglenandu.foodigo.foodigo.Security;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
@Entity
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Username cannot be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Username must only contain alphanumeric characters and underscores.")
    @Size(min = 3, max = 20, message = "Minimum 3 characters are required.")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|:;,.<>?]).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    @Column(nullable = false)
    private String password;

    @Email(message = "Invalid email format")
    @Column(nullable = true, unique = true)
    private String email;

    @Pattern(regexp = "^\\+97798\\d{7}$", message = "Invalid phone number. It must start with +97798 and be 10 digits long.")
    @Column(nullable = true, unique = true)
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
}
