package group3.paws_hope.dto.res;

import group3.paws_hope.entity.ItemDonation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class ItemDonationRes {
    private Long itemDonationId;
    private Long userId;
    private String donorNameManual;
    private String itemName;
    private Long receivedBy;
    private String note;
    private String category;
    private String quantity;
    private String status;
    private Timestamp receivedAt;

    public static ItemDonationRes toJson(ItemDonation itemDonation) {
        return new ItemDonationRes(
                itemDonation.getItemDonationId(),
                itemDonation.getUser() != null ? itemDonation.getUser().getUserId() : null,
                itemDonation.getDonorNameManual(),
                itemDonation.getItemName(),
                itemDonation.getReceivedBy() != null ? itemDonation.getReceivedBy().getUserId() : null,
                itemDonation.getNote(),
                itemDonation.getCategory() != null ? itemDonation.getCategory().name() : null,
                itemDonation.getQuantity(),
                itemDonation.getStatus() != null ? itemDonation.getStatus().name() : null,
                itemDonation.getReceivedAt()
        );
    }
}