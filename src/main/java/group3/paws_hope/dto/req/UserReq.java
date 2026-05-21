package group3.paws_hope.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReq {
    private Long user_id;

    @NotBlank(message = "Username cannot be left blank.")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.")
    private String username;

    @NotBlank(message = "Password cannot be left blank.")
    private String passwordHash;

    @NotBlank(message = "The full name cannot be left blank.")
    private String fullName;

    @Email(message = "Email is not in the correct format.")
    @NotBlank(message = "Email cannot be left blank")

    private String email;
    private String phone;
    private String role;
    private Integer status;
}