package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationReq {

    @NotNull(message = "User id cannot be null.")
    private Long userId;

    @NotBlank(message = "Message cannot be left blank.")
    private String message;

    private String type;

    private Long relatedId;
}