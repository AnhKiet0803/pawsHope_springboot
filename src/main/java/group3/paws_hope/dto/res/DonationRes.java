package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Donation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class DonationRes {
    private Long donationId;
    private Long campaignId;
    private Long userId;
    private String donorNameManual;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private Long sourceOrderId;
    private String donationType;
    private Timestamp receivedAt;

    public static DonationRes toJson(Donation donation) {
        return new DonationRes(
                donation.getDonationId(),
                donation.getCampaign().getCampaignId(),
                donation.getUser() != null ? donation.getUser().getUserId() : null,
                donation.getDonorNameManual(),
                donation.getAmount(),
                donation.getPaymentMethod().name(),
                donation.getPaymentStatus().name(),
                donation.getSourceOrder() != null ? donation.getSourceOrder().getOrderId() : null,
                donation.getDonationType().name(),
                donation.getReceivedAt()
        );
    }
}