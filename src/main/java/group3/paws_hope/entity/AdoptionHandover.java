package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_handovers")
@Getter
@Setter
public class AdoptionHandover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "handover_id")
    private Long handoverId;

    @ManyToOne
    @JoinColumn(name = "adoption_id", nullable = false)
    private Adoption adoption;

    @ManyToOne
    @JoinColumn(name = "handled_by")
    private User handledBy;

    @Column(name = "pickup_datetime", nullable = false)
    private LocalDateTime pickupDatetime;

    @Column(name = "pickup_location", columnDefinition = "TEXT")
    private String pickupLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "handover_method")
    private HandoverMethod handoverMethod = HandoverMethod.AT_SHELTER;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    @Column(name = "adopter_confirmed")
    private Boolean adopterConfirmed = false;

    @Column(name = "items_given", columnDefinition = "TEXT")
    private String itemsGiven;

    @Column(name = "handover_photo_url", length = 255)
    private String handoverPhotoUrl;

    @Column(name = "completion_note", columnDefinition = "TEXT")
    private String completionNote;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum HandoverMethod {
        AT_SHELTER, HOME_VISIT, MEETUP_POINT
    }

    public enum Status {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
    }
}