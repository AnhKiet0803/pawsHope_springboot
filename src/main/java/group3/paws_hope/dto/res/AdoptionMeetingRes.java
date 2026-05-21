package group3.paws_hope.dto.res;

import group3.paws_hope.entity.AdoptionMeeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class AdoptionMeetingRes {

    private Long meetingId;
    private Long adoptionId;
    private Long staffId;
    private LocalDateTime meetingDatetime;
    private String meetingLocation;
    private String status;
    private String result;
    private String housingCheckResult;
    private String experienceEvaluation;
    private String note;
    private Timestamp createdAt;

    public static AdoptionMeetingRes toJson(AdoptionMeeting meeting) {
        return new AdoptionMeetingRes(
                meeting.getMeetingId(),
                meeting.getAdoption().getAdoptionId(),
                meeting.getStaff().getUserId(),
                meeting.getMeetingDatetime(),
                meeting.getMeetingLocation(),
                meeting.getStatus().name(),
                meeting.getResult().name(),
                meeting.getHousingCheckResult(),
                meeting.getExperienceEvaluation(),
                meeting.getNote(),
                meeting.getCreatedAt()
        );
    }
}