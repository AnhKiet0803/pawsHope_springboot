package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_interviews")
@Getter
@Setter
public class VolunteerInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long interviewId;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private VolunteerApplication application;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "interview_datetime", nullable = false)
    private LocalDateTime interviewDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type")
    private MeetingType meetingType = MeetingType.ONLINE;

    @Column(name = "meeting_link", length = 255)
    private String meetingLink;

    @Column(name = "location_text", columnDefinition = "TEXT")
    private String locationText;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    @Enumerated(EnumType.STRING)
    private Result result = Result.PENDING;

    @Column(name = "evaluation_note", columnDefinition = "TEXT")
    private String evaluationNote;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum MeetingType {
        ONLINE, OFFLINE
    }

    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    }

    public enum Result {
        PENDING, PASSED, FAILED
    }
}