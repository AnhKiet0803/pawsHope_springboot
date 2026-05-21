package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderRes {
    private Long orderId;
    private Long userId;
    private BigDecimal subtotalAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private String shippingAddress;
    private String receiverName;
    private String receiverPhone;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItemRes> items;

    public static OrderRes toJson(Order order, List<OrderItemRes> items) {
        return new OrderRes(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getSubtotalAmount(),
                order.getShippingFee(),
                order.getTotalAmount(),
                order.getPaymentMethod().name(),
                order.getPaymentStatus().name(),
                order.getOrderStatus().name(),
                order.getShippingAddress(),
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getNote(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items
        );
    }
}