package group3.paws_hope.dto.res;

import group3.paws_hope.entity.VolunteerApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class VolunteerApplicationRes {
    private Long applicationId;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String occupation;
    private String skills;
    private String experienceWithAnimals;
    private String reasonToJoin;
    private String availableDays;
    private String preferredTasks;
    private Boolean hasTransport;
    private String status;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private String rejectionReason;
    private Timestamp appliedAt;

    public static VolunteerApplicationRes toJson(VolunteerApplication app) {
        return new VolunteerApplicationRes(
                app.getApplicationId(),
                app.getUser() != null ? app.getUser().getUserId() : null,
                app.getFullName(),
                app.getEmail(),
                app.getPhone(),
                app.getDateOfBirth(),
                app.getAddress(),
                app.getOccupation(),
                app.getSkills(),
                app.getExperienceWithAnimals(),
                app.getReasonToJoin(),
                app.getAvailableDays(),
                app.getPreferredTasks(),
                app.getHasTransport(),
                app.getStatus().name(),
                app.getReviewedBy() != null ? app.getReviewedBy().getUserId() : null,
                app.getReviewedAt(),
                app.getRejectionReason(),
                app.getAppliedAt()
        );
    }
}