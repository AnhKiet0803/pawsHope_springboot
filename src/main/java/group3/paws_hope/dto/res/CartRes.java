package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class CartRes {
    private Long cartId;
    private Long userId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static CartRes toJson(Cart cart) {
        BigDecimal total = cart.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cart.getQuantity()));

        return new CartRes(
                cart.getCartId(),
                cart.getUser().getUserId(),
                cart.getProduct().getProductId(),
                cart.getProduct().getProductName(),
                cart.getProduct().getPrice(),
                cart.getQuantity(),
                total,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}