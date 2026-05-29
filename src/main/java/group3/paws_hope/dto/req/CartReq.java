package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartReq {

    @NotNull(message = "Product id cannot be null.")
    private Long productId;

    @NotNull(message = "Quantity cannot be null.")
    private Integer quantity;
}