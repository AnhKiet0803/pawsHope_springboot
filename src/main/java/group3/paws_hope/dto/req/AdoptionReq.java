package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdoptionReq {

    @NotNull(message = "Pet id cannot be null.")
    private Long petId;

    @NotNull(message = "User id cannot be null.")
    private Long userId;

    private String applicantAddress;
    private String housingType;
    private Boolean hasPetExperience;
    private String currentPets;
    private String workingSchedule;
    private String reason;
    private Boolean familyAgreement;
    private Boolean financialCommitment;
    private String priorityLevel;
    private String reviewStatus;
    private String missingInfoNote;
    private BigDecimal adoptionFee;
    private String notes;
}