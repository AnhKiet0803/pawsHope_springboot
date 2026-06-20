package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetStatusLogReq {

    @NotNull(message = "Pet ID cannot be null.")
    private Long petId; // Cần cái này để biết đang update cho con nào

    private String oldStatus; // Trạng thái cũ (để ghi log)

    @NotBlank(message = "New status cannot be left blank.")
    private String newStatus;

    private String note;

    @NotNull(message = "Updated by cannot be null.")
    private Long updatedBy;
}