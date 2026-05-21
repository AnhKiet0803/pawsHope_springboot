package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VolunteerScheduleReq {

    @NotNull(message = "Week id cannot be null.")
    private Long weekId;

    @NotNull(message = "User id cannot be null.")
    private Long userId;

    @NotNull(message = "Shift id cannot be null.")
    private Long shiftId;

    @NotNull(message = "Work date cannot be null.")
    private LocalDate workDate;
}