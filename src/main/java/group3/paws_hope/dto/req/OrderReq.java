package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderReq {

    @NotNull(message = "User id cannot be null.")
    private Long userId;

    private BigDecimal shippingFee;

    @NotBlank(message = "Shipping address cannot be left blank.")
    private String shippingAddress;

    @NotBlank(message = "Receiver name cannot be left blank.")
    private String receiverName;

    @NotBlank(message = "Receiver phone cannot be left blank.")
    private String receiverPhone;

    private String note;
}