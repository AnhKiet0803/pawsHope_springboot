package group3.paws_hope.dto.res;

import group3.paws_hope.entity.VolunteerSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class VolunteerScheduleRes {
    private Long scheduleId;
    private Long windowId;
    private Long weekId;
    private Long userId;
    private String volunteerName;
    private Long shiftId;
    private String shiftName;
    private LocalDate workDate;
    private Timestamp registeredAt;
    private String weekStatus;

    public static VolunteerScheduleRes toJson(VolunteerSchedule schedule) {
        return new VolunteerScheduleRes(
                schedule.getScheduleId(),
                schedule.getWeek().getWindow().getWindowId(),
                schedule.getWeek().getWeekId(),
                schedule.getUser().getUserId(),
                schedule.getUser().getFullName(),
                schedule.getShift().getShiftId(),
                schedule.getShift().getShiftName(),
                schedule.getWorkDate(),
                schedule.getRegisteredAt(),
                schedule.getWeek().getStatus() != null ? schedule.getWeek().getStatus().name() : "DRAFT"
        );
    }
}