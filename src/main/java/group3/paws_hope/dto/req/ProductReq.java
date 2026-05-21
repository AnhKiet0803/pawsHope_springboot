package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductReq {

    @NotBlank(message = "Product name cannot be left blank.")
    private String productName;

    private String description;

    @NotNull(message = "Price cannot be null.")
    private BigDecimal price;

    private Integer stockQuantity;
    private String imageUrl;
    private Boolean isActive;
}