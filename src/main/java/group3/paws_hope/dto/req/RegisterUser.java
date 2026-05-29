package group3.paws_hope.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUser {

    @NotBlank(message = "Please enter username")
    private String username;

    @NotBlank(message = "Please enter your name")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Please enter your email")
    private String email;

    private String phone;

    @NotBlank(message = "Please enter password")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}