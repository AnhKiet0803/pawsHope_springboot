package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_followups")
@Getter
@Setter
public class AdoptionFollowup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private Long followupId;

    @ManyToOne
    @JoinColumn(name = "adoption_id", nullable = false)
    private Adoption adoption;

    @Column(name = "followup_date", nullable = false)
    private LocalDate followupDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "followup_type")
    private FollowupType followupType = FollowupType.MESSAGE;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_condition")
    private PetCondition petCondition = PetCondition.GOOD;

    @Column(name = "adopter_feedback", columnDefinition = "TEXT")
    private String adopterFeedback;

    @Column(name = "staff_note", columnDefinition = "TEXT")
    private String staffNote;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Column(name = "next_followup_date")
    private LocalDate nextFollowupDate;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum FollowupType {
        PHONE_CALL, MESSAGE, PHOTO_UPDATE, HOME_VISIT, HEALTH_CHECK
    }

    public enum Status {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_RESPONSE
    }

    public enum PetCondition {
        EXCELLENT, GOOD, NORMAL, NEEDS_ATTENTION, URGENT
    }
}