package group3.paws_hope.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRes {
    private String token;
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
}