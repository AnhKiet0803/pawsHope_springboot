package group3.paws_hope.dto.res;

import group3.paws_hope.entity.VolunteerScheduleWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class VolunteerScheduleWeekRes {

    private Long weekId;
    private Long windowId;
    private Long userId;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String status;
    private LocalDateTime submittedAt;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static VolunteerScheduleWeekRes toJson(VolunteerScheduleWeek week) {
        return new VolunteerScheduleWeekRes(
                week.getWeekId(),
                week.getWindow().getWindowId(),
                week.getUser().getUserId(),
                week.getWeekStartDate(),
                week.getWeekEndDate(),
                week.getStatus().name(),
                week.getSubmittedAt(),
                week.getApprovedBy() != null ? week.getApprovedBy().getUserId() : null,
                week.getApprovedAt(),
                week.getRejectionReason(),
                week.getCreatedAt(),
                week.getUpdatedAt()
        );
    }
}