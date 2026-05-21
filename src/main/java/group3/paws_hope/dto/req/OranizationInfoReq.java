package group3.paws_hope.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OranizationInfoReq {
    @NotBlank(message = "Organization name is required")
    private String orgName;

    private String logoUrl;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String hotline;

    @Email(message = "Email should be valid")
    private String email;

    private String facebookLink;

    private String address;

    private String missionStatement;
}
