package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VolunteerInterviewReq {

    private Long interviewId;

    @NotNull(message = "Application id cannot be null.")
    private Long applicationId;

    @NotNull(message = "Interviewer id cannot be null.")
    private Long interviewerId;

    @NotNull(message = "Interview datetime cannot be null.")
    private LocalDateTime interviewDatetime;

    private String meetingType;
    private String meetingLink;
    private String locationText;
    private String status;
    private String result;
    private String evaluationNote;
}