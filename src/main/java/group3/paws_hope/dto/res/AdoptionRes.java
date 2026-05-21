package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Adoption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class AdoptionRes {
    private Long adoptionId;
    private String applicationCode;
    private Long petId;
    private Long userId;
    private String applicantAddress;
    private String housingType;
    private Boolean hasPetExperience;
    private String currentPets;
    private String workingSchedule;
    private String reason;
    private Boolean familyAgreement;
    private Boolean financialCommitment;
    private LocalDate applyDate;
    private String status;
    private String priorityLevel;
    private String reviewStatus;
    private String missingInfoNote;
    private BigDecimal adoptionFee;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paidAt;
    private String notes;
    private Long processedBy;
    private LocalDateTime reviewedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static AdoptionRes toJson(Adoption adoption) {
        return new AdoptionRes(
                adoption.getAdoptionId(),
                adoption.getApplicationCode(),
                adoption.getPet().getPetId(),
                adoption.getUser().getUserId(),
                adoption.getApplicantAddress(),
                adoption.getHousingType() != null ? adoption.getHousingType().name() : null,
                adoption.getHasPetExperience(),
                adoption.getCurrentPets(),
                adoption.getWorkingSchedule(),
                adoption.getReason(),
                adoption.getFamilyAgreement(),
                adoption.getFinancialCommitment(),
                adoption.getApplyDate(),
                adoption.getStatus().name(),
                adoption.getPriorityLevel().name(),
                adoption.getReviewStatus().name(),
                adoption.getMissingInfoNote(),
                adoption.getAdoptionFee(),
                adoption.getPaymentMethod().name(),
                adoption.getPaymentStatus().name(),
                adoption.getPaidAt(),
                adoption.getNotes(),
                adoption.getProcessedBy() != null ? adoption.getProcessedBy().getUserId() : null,
                adoption.getReviewedAt(),
                adoption.getCreatedAt(),
                adoption.getUpdatedAt()
        );
    }
}