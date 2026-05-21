package group3.paws_hope.dto.res;

import group3.paws_hope.entity.DonationCampaign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class DonationCampaignRes {
    private Long campaignId;
    private String title;
    private String description;
    private BigDecimal targetAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long createdBy;
    private Timestamp createdAt;

    public static DonationCampaignRes toJson(DonationCampaign campaign) {
        return new DonationCampaignRes(
                campaign.getCampaignId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getTargetAmount(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getStatus().name(),
                campaign.getCreatedBy() != null ? campaign.getCreatedBy().getUserId() : null,
                campaign.getCreatedAt()
        );
    }
}