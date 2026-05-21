package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Table(name = "rescue_reports")
@Getter
@Setter
public class RescueReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reporter_name", length = 100)
    private String reporterName;

    @Column(name = "reporter_phone", length = 20, nullable = false)
    private String reporterPhone;

    @Column(name = "location_text", nullable = false, columnDefinition = "TEXT")
    private String locationText;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level")
    private UrgencyLevel urgencyLevel = UrgencyLevel.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "injury_type")
    private InjuryType injuryType = InjuryType.NONE;

    @Enumerated(EnumType.STRING)
    private Temperament temperament = Temperament.SCARED;

    @Enumerated(EnumType.STRING)
    private Behavior behavior = Behavior.ACTIVE;

    @Column(name = "additional_note", columnDefinition = "TEXT")
    private String additionalNote;

    @Column(name = "image_url",length = 255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "tracking_code", length = 20, unique = true)
    private String trackingCode;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    public enum UrgencyLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum InjuryType {
        NONE, BLEEDING, BROKEN_BONE, DISEASE, OTHER
    }

    public enum Temperament {
        FRIENDLY, SCARED, AGGRESSIVE
    }

    public enum Behavior {
        ACTIVE, IMMOBILE, LIMPING
    }

    public enum Status {
        PENDING, IN_PROGRESS, RESCUED, FAILED
    }
}