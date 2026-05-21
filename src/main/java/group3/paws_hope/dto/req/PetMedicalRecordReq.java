package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetMedicalRecordReq {
    @NotNull(message = "Pet id cannot be null.")
    private Long petId;

    @NotNull(message = "Record type cannot be null.")
    private String recordType;

    @NotNull(message = "Record date cannot be null.")
    private LocalDate recordDate;

    private LocalDate nextDueDate;
    private String description;
    private Long createdBy;
}