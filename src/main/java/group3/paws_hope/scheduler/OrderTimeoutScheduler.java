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
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void releaseExpiredOrders() {

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(4);
        Timestamp timeout = Timestamp.valueOf(threshold);

        List<Order> expiredOrders =
                orderRepository.findByOrderStatusAndCreatedAtBefore(
                        Order.OrderStatus.PENDING_PAYMENT,
                        timeout
                );

        for (Order order : expiredOrders) {

            // 🔥 IMPORTANT: tránh cancel order đã paid
            if (order.getPaymentStatus() != Order.PaymentStatus.PENDING) {
                continue;
            }

            order.setOrderStatus(Order.OrderStatus.CANCELLED);
            order.setPaymentStatus(Order.PaymentStatus.FAILED);

            List<OrderItem> items =
                    orderItemRepository.findByOrder_OrderId(order.getOrderId());

            for (OrderItem item : items) {
                Product product = item.getProduct();
                product.setStockQuantity(
                        product.getStockQuantity() + item.getQuantity()
                );
                productRepository.save(product);
            }

            orderRepository.save(order);

            System.out.println("[SCHEDULER] Cancelled order #" + order.getOrderId());
        }
    }
}