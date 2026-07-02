package group3.paws_hope.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactMessageReq {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private String subject = "OTHER";

    @NotBlank
    private String message;
}
