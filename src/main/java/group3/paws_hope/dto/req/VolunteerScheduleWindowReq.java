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

    @NotNull(message = "Week end date cannot be null.")
    private LocalDate weekEndDate;

    @NotNull(message = "Open time cannot be null.")
    private LocalDateTime openAt;

    @NotNull(message = "Close time cannot be null.")
    private LocalDateTime closeAt;

    private String status;
    private Long createdBy;
}