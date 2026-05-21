package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class ShiftRes {
    private Long shiftId;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean crossesMidnight;

    public static ShiftRes toJison(Shift shift) {
        return new ShiftRes(
                shift.getShiftId(),
                shift.getShiftName(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getCrossesMidnight()
        );
    }
}