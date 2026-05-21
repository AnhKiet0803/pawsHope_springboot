package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoptions")
@Getter
@Setter
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private Long adoptionId;

    @Column(name = "application_code", length = 20, unique = true)
    private String applicationCode;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "applicant_address", columnDefinition = "TEXT")
    private String applicantAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "housing_type")
    private HousingType housingType;

    @Column(name = "has_pet_experience")
    private Boolean hasPetExperience = false;

    @Column(name = "current_pets", columnDefinition = "TEXT")
    private String currentPets;

    @Column(name = "working_schedule", length = 255)
    private String workingSchedule;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "family_agreement")
    private Boolean familyAgreement = true;

    @Column(name = "financial_commitment")
    private Boolean financialCommitment = true;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level")
    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus = ReviewStatus.NORMAL;

    @Column(name = "missing_info_note", columnDefinition = "TEXT")
    private String missingInfoNote;

    @Column(name = "adoption_fee")
    private BigDecimal adoptionFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod = PaymentMethod.PAYPAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    public enum HousingType {
        APARTMENT, HOUSE, DORMITORY, OTHER
    }

    public enum Status {
        PENDING, MEETING_SCHEDULED, INTERVIEWING, APPROVED, REJECTED, HANDOVER_SCHEDULED, COMPLETED, CANCELLED
    }

    public enum PriorityLevel {
        LOW, MEDIUM, HIGH
    }

    public enum ReviewStatus {
        NORMAL, NEED_MORE_INFO, SUSPICIOUS, PRIORITY
    }

    public enum PaymentMethod {
        PAYPAL
    }

    public enum PaymentStatus {
        UNPAID, PAID, WAIVED
    }
}