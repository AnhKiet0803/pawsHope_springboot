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
    private Long weekId;
    private Long userId;
    private Long shiftId;
    private LocalDate workDate;
    private Timestamp registeredAt;

    public static VolunteerScheduleRes toJson(VolunteerSchedule schedule) {
        return new VolunteerScheduleRes(
                schedule.getScheduleId(),
                schedule.getWeek().getWeekId(),
                schedule.getUser().getUserId(),
                schedule.getShift().getShiftId(),
                schedule.getWorkDate(),
                schedule.getRegisteredAt()
        );
    }
}