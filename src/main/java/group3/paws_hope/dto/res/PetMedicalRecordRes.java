package group3.paws_hope.dto.res;

import group3.paws_hope.entity.PetMedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PetMedicalRecordRes {
    private Long medicalId;
    private Long petId;
    private String recordType;
    private LocalDate recordDate;
    private LocalDate nextDueDate;
    private String description;
    private Long createdBy;
    private Timestamp createdAt;

    public static PetMedicalRecordRes toJson(PetMedicalRecord record) {
        return new PetMedicalRecordRes(
                record.getMedicalId(),
                record.getPet().getPetId(),
                record.getRecordType().name(),
                record.getRecordDate(),
                record.getNextDueDate(),
                record.getDescription(),
                record.getCreatedBy() != null ? record.getCreatedBy().getUserId() : null,
                record.getCreatedAt()
        );
    }
}