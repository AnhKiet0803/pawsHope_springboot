package group3.paws_hope.dto.res;

import group3.paws_hope.entity.VolunteerInterview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class VolunteerInterviewRes {

    private Long interviewId;
    private Long applicationId;
    private Long interviewerId;
    private LocalDateTime interviewDatetime;
    private String meetingType;
    private String meetingLink;
    private String locationText;
    private String status;
    private String result;
    private String evaluationNote;
    private Timestamp createdAt;

    public static VolunteerInterviewRes toJson(VolunteerInterview interview) {
        return new VolunteerInterviewRes(
                interview.getInterviewId(),
                interview.getApplication().getApplicationId(),
                interview.getInterviewer().getUserId(),
                interview.getInterviewDatetime(),
                interview.getMeetingType().name(),
                interview.getMeetingLink(),
                interview.getLocationText(),
                interview.getStatus().name(),
                interview.getResult().name(),
                interview.getEvaluationNote(),
                interview.getCreatedAt()
        );
    }
}