package group3.paws_hope.service;

import group3.paws_hope.dto.req.OrderReq;
import group3.paws_hope.dto.res.OrderItemRes;
import group3.paws_hope.dto.res.OrderRes;
import group3.paws_hope.entity.*;
import group3.paws_hope.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<OrderRes> getAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToRes)
                .toList();
    }

    public OrderRes findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToRes(order);
    }

    public List<OrderRes> getByUserId(Long userId) {
        return orderRepository.findByUser_UserId(userId).stream()
                .map(this::convertToRes)
                .toList();
    }

    public OrderRes getPendingOrder(Long userId) {

        Order order = orderRepository
                .findFirstByUser_UserIdAndOrderStatusAndPaymentStatus(
                        userId,
                        Order.OrderStatus.PENDING_PAYMENT,
                        Order.PaymentStatus.PENDING
                )
                .orElse(null);

        if (order == null) {
            return null;
        }

        return convertToRes(order);
    }

    @Transactional
    public OrderRes createFromCart(OrderReq req) {
        try {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Order existingOrder = orderRepository
                    .findFirstByUser_UserIdAndOrderStatusAndPaymentStatus(
                            req.getUserId(),
                            Order.OrderStatus.PENDING_PAYMENT,
                            Order.PaymentStatus.PENDING
                    )
                    .orElse(null);

            if (existingOrder != null) {
                return convertToRes(existingOrder);
            }

            List<Cart> cartItems = cartRepository.findByUser_UserId(req.getUserId());
            System.out.println("Cart size = " + cartItems.size());

            if (cartItems.isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            BigDecimal subtotal = BigDecimal.ZERO;

            for (Cart cart : cartItems) {
                Product product = cart.getProduct();

                if (!product.getIsActive()) {
                    throw new RuntimeException("Product is not active: " + product.getProductName());
                }

                if (cart.getQuantity() > product.getStockQuantity()) {
                    throw new RuntimeException("Not enough stock for product: " + product.getProductName());
                }

                subtotal = subtotal.add(
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))
                );
            }

            BigDecimal shippingFee = req.getShippingFee() != null
                    ? req.getShippingFee()
                    : BigDecimal.ZERO;

            BigDecimal total = subtotal.add(shippingFee);

            Order order = new Order();
            order.setUser(user);
            order.setSubtotalAmount(subtotal);
            order.setShippingFee(shippingFee);
            order.setTotalAmount(total);
            order.setPaymentMethod(Order.PaymentMethod.PAYPAL);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
            order.setOrderStatus(Order.OrderStatus.PENDING_PAYMENT);
            order.setShippingAddress(req.getShippingAddress());
            order.setReceiverName(req.getReceiverName());
            order.setReceiverPhone(req.getReceiverPhone());
            order.setNote(req.getNote());

            Order savedOrder = orderRepository.save(order);

            return convertToRes(savedOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public OrderRes finishOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Long userId = order.getUser().getUserId();

        List<Cart> cartItems = cartRepository.findByUser_UserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        for (Cart cart : cartItems) {

            Product product = cart.getProduct();

            if (cart.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getProductName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductNameSnapshot(product.getProductName());
            item.setQuantity(cart.getQuantity());
            item.setPriceAtPurchase(product.getPrice());

            orderItemRepository.save(item);

            product.setStockQuantity(
                    product.getStockQuantity() - cart.getQuantity());

            productRepository.save(product);
        }

        cartRepository.deleteByUser_UserId(userId);

        order.setPaymentStatus(Order.PaymentStatus.PAID);
        order.setOrderStatus(Order.OrderStatus.CONFIRMED);

        orderRepository.save(order);

        return convertToRes(order);
    }

    public OrderRes updateOrderStatus(Long id, String status) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.setOrderStatus(Order.OrderStatus.valueOf(status));

            return convertToRes(orderRepository.save(order));
        } catch (Exception e) {
            return null;
        }
    }

    public OrderRes updatePaymentStatus(Long id, String status) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.setPaymentStatus(Order.PaymentStatus.valueOf(status));

            return convertToRes(orderRepository.save(order));
        } catch (Exception e) {
            return null;
        }
    }

    private OrderRes convertToRes(Order order) {
        List<OrderItemRes> items = orderItemRepository.findByOrder_OrderId(order.getOrderId())
                .stream()
                .map(OrderItemRes::toJson)
                .toList();
        return OrderRes.toJson(order, items);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            return;
        }

        order.setOrderStatus(Order.OrderStatus.CANCELLED);

        List<OrderItem> items = orderItemRepository.findByOrder_OrderId(orderId);

        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setStockQuantity(
                    product.getStockQuantity() + item.getQuantity()
            );
            productRepository.save(product);
        }

        orderRepository.save(order);
    }
}