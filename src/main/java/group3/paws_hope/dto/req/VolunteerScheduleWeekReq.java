package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolunteerScheduleWeekReq {

    @NotNull(message = "Window id cannot be null.")
    private Long windowId;

    @NotNull(message = "User id cannot be null.")
    private Long userId;
}