package nigglenandu.foodigo.foodigo.Security.Paylaod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private List<String> roles;
    private Long userId;
    private String username;
    private String email;

}
