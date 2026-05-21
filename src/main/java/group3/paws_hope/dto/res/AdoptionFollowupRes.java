package group3.paws_hope.dto.res;

import group3.paws_hope.entity.AdoptionFollowup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class AdoptionFollowupRes {

    private Long followupId;
    private Long adoptionId;
    private LocalDate followupDate;
    private String followupType;
    private String status;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private String petCondition;
    private String adopterFeedback;
    private String staffNote;
    private String photoUrl;
    private LocalDate nextFollowupDate;
    private Long createdBy;
    private Timestamp createdAt;

    public static AdoptionFollowupRes toJson(AdoptionFollowup followup) {
        return new AdoptionFollowupRes(
                followup.getFollowupId(),
                followup.getAdoption().getAdoptionId(),
                followup.getFollowupDate(),
                followup.getFollowupType().name(),
                followup.getStatus().name(),
                followup.getConfirmedAt(),
                followup.getCompletedAt(),
                followup.getPetCondition().name(),
                followup.getAdopterFeedback(),
                followup.getStaffNote(),
                followup.getPhotoUrl(),
                followup.getNextFollowupDate(),
                followup.getCreatedBy() != null ? followup.getCreatedBy().getUserId() : null,
                followup.getCreatedAt()
        );
    }
}