package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VolunteerScheduleWindowReq {

    private Long windowId;

    @NotNull(message = "Week start date cannot be null.")
    private LocalDate weekStartDate;

    private LocalDate weekEndDate;

    private LocalDateTime openAt;

    private LocalDateTime closeAt;

    private String status;
    private Long createdBy;
}