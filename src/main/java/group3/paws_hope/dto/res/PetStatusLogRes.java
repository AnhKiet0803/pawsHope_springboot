package group3.paws_hope.dto.res;

import group3.paws_hope.entity.PetStatusLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class PetStatusLogRes {
    private Long logId;
    private Long petId;
    private String oldStatus;
    private String newStatus;
    private String note;
    private Long updatedBy;
    private Timestamp updatedAt;

    public static PetStatusLogRes toJson(PetStatusLog log) {
        return new PetStatusLogRes(
                log.getLogId(),
                log.getPet().getPetId(),
                log.getOldStatus() != null ? log.getOldStatus().name() : null,
                log.getNewStatus().name(),
                log.getNote(),
                log.getUpdatedBy().getUserId(),
                log.getUpdatedAt()
        );
    }
}