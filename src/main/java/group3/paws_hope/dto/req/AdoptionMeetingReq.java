package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdoptionMeetingReq {

    @NotNull(message = "Adoption id cannot be null.")
    private Long adoptionId;

    @NotNull(message = "Staff id cannot be null.")
    private Long staffId;

    @NotNull(message = "Meeting datetime cannot be null.")
    private LocalDateTime meetingDatetime;

    private String meetingLocation;
    private String status;
    private String result;
    private String housingCheckResult;
    private String experienceEvaluation;
    private String note;
}