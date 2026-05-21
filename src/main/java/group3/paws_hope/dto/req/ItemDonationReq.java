package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDonationReq {
    private Long userId;
    private String donorNameManual;

    @NotBlank(message = "Item name cannot be left blank.")
    private String itemName;

    private Long receivedBy;
    private String note;
    private String category;
    private String quantity;
    private String status;
}