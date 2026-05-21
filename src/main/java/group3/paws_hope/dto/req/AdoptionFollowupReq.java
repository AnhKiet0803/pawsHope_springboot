package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdoptionFollowupReq {

    @NotNull(message = "Adoption id cannot be null.")
    private Long adoptionId;

    private LocalDate followupDate;
    private String followupType;
    private String petCondition;
    private String adopterFeedback;
    private String staffNote;
    private String photoUrl;

    @NotNull(message = "Created by cannot be null.")
    private Long createdBy;
}