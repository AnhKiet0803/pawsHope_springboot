package group3.paws_hope.security;

import group3.paws_hope.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("cartSecurity")
@AllArgsConstructor
public class CartSecurity {

    private final CartRepository cartRepository;

    public boolean isOwner(Long cartId, String usernameOrEmail) {
        return cartRepository.findById(cartId)
                .map(cart -> {
                    var user = cart.getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }
}
