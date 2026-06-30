package group3.paws_hope.scheduler;

import group3.paws_hope.entity.Order;
import group3.paws_hope.entity.OrderItem;
import group3.paws_hope.entity.Product;
import group3.paws_hope.repository.OrderItemRepository;
import group3.paws_hope.repository.OrderRepository;
import group3.paws_hope.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class OrderTimeoutScheduler {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void releaseExpiredOrders() {
        LocalDateTime fourMinutesAgo = LocalDateTime.now().minusMinutes(4);
        Timestamp timeoutThreshold = Timestamp.valueOf(fourMinutesAgo);
        List<Order> expiredOrders = orderRepository.findByOrderStatusAndCreatedAtBefore(
                Order.OrderStatus.PENDING_PAYMENT,
                timeoutThreshold
        );

        if (!expiredOrders.isEmpty()) {
            System.out.println("[SCHEDULER] Detect " + expiredOrders.size() + " Order payment overdue by 4 minutes.");

            for (Order order : expiredOrders) {
                order.setOrderStatus(Order.OrderStatus.CANCELLED);
                orderRepository.save(order);

                List<OrderItem> items = orderItemRepository.findByOrder_OrderId(order.getOrderId());
                for (OrderItem item : items) {
                    Product product = item.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                    productRepository.save(product);
                    System.out.println(
                            "[SCHEDULER] Refunded "
                                    + item.getQuantity()
                                    + " Product [" + product.getProductName()
                                    + "] to the warehouse."
                    );
                }

                System.out.println("[SCHEDULER] Successfully cancelled order number: #" + order.getOrderId());
            }
        }
    }
}