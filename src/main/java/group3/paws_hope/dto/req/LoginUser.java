package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginUser {
    @NotBlank(message = "Please enter your email")
    private String email;

    @NotBlank(message = "Please enter your password")
    private String password;
}

