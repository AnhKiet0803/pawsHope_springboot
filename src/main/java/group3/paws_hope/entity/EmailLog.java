package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Getter
@Setter
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @Column(name = "recipient_email", nullable = false, length = 100)
    private String recipientEmail;

    @Column(name = "recipient_name", length = 100)
    private String recipientName;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type")
    private EmailType emailType = EmailType.SYSTEM;

    @Column(name = "related_table", length = 50)
    private String relatedTable;

    @Column(name = "related_id")
    private Long relatedId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "sent_by")
    private User sentBy;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum EmailType {
        VOLUNTEER_INTERVIEW, VOLUNTEER_RESULT, ADOPTION_MEETING,
        ADOPTION_RESULT, ADOPTION_HANDOVER, ADOPTION_FOLLOWUP, ORDER_STATUS, DONATION, SYSTEM
    }

    public enum Status {
        PENDING, SENT, FAILED
    }
}