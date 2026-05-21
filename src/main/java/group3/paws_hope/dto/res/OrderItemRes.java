package group3.paws_hope.dto.res;

import group3.paws_hope.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class OrderItemRes {
    private Long orderItemId;
    private Long productId;
    private String productNameSnapshot;
    private Integer quantity;
    private BigDecimal priceAtPurchase;

    public static OrderItemRes toJson(OrderItem item) {
        return new OrderItemRes(
                item.getOrderItemId(),
                item.getProduct().getProductId(),
                item.getProductNameSnapshot(),
                item.getQuantity(),
                item.getPriceAtPurchase()
        );
    }
}