package group3.paws_hope.security;

import group3.paws_hope.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
@AllArgsConstructor
public class OrderSecurity {
    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, String usernameOrEmail) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    var user = order.getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }
}