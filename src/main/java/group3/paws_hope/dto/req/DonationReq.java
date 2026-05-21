package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DonationReq {

    @NotNull(message = "Campaign id cannot be null.")
    private Long campaignId;

    private Long userId;
    private String donorNameManual;

    @NotNull(message = "Amount cannot be null.")
    private BigDecimal amount;

    private String paymentStatus;
    private Long sourceOrderId;

    @NotNull(message = "Donation type cannot be null.")
    private String donationType;
}