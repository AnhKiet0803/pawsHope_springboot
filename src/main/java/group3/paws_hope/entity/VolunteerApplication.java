package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_applications")
@Getter
@Setter
public class VolunteerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String occupation;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "experience_with_animals", columnDefinition = "TEXT")
    private String experienceWithAnimals;

    @Column(name = "reason_to_join", columnDefinition = "TEXT")
    private String reasonToJoin;

    @Column(name = "available_days")
    private String availableDays;

    @Column(name = "preferred_tasks")
    private String preferredTasks;

    @Column(name = "has_transport")
    private Boolean hasTransport = false;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "applied_at", insertable = false, updatable = false)
    private Timestamp appliedAt;

    public enum Status {
        PENDING, INTERVIEW_SCHEDULED, INTERVIEWED, APPROVED, REJECTED
    }
}
