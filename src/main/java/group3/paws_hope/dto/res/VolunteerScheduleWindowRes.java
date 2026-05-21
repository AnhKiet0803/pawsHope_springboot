package group3.paws_hope.dto.res;

import group3.paws_hope.entity.VolunteerScheduleWindow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class VolunteerScheduleWindowRes {

    private Long windowId;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private String status;
    private Long createdBy;
    private Timestamp createdAt;

    public static VolunteerScheduleWindowRes toJson(VolunteerScheduleWindow window) {
        return new VolunteerScheduleWindowRes(
                window.getWindowId(),
                window.getWeekStartDate(),
                window.getWeekEndDate(),
                window.getOpenAt(),
                window.getCloseAt(),
                window.getStatus().name(),
                window.getCreatedBy() != null ? window.getCreatedBy().getUserId() : null,
                window.getCreatedAt()
        );
    }
}