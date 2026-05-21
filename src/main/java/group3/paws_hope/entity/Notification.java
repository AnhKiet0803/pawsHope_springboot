package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Long notiId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private Type type = Type.SYSTEM;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum Type {
        ADOPTION_MEETING, ADOPTION_HANDOVER, ADOPTION_FOLLOWUP,
        RESCUE_ASSIGNED, ORDER_STATUS, DONATION, VOLUNTEER_INTERVIEW, VOLUNTEER_RESULT, SYSTEM
    }
}