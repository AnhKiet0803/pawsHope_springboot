package group3.paws_hope.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftReq {
    private Long shiftId;

    @NotBlank(message = "Choose your shift")
    private String shiftName;

    @NotNull()
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull()
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private Boolean crossesMidnight;
}