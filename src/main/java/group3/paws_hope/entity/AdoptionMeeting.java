package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_meetings")
@Getter
@Setter
public class AdoptionMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long meetingId;

    @ManyToOne
    @JoinColumn(name = "adoption_id", nullable = false)
    private Adoption adoption;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Column(name = "meeting_datetime", nullable = false)
    private LocalDateTime meetingDatetime;

    @Column(name = "meeting_location", columnDefinition = "TEXT")
    private String meetingLocation;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    @Enumerated(EnumType.STRING)
    private Result result = Result.PENDING;

    @Column(name = "housing_check_result", columnDefinition = "TEXT")
    private String housingCheckResult;

    @Column(name = "experience_evaluation", columnDefinition = "TEXT")
    private String experienceEvaluation;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED
    }

    public enum Result {
        PENDING, PASSED, FAILED
    }
}