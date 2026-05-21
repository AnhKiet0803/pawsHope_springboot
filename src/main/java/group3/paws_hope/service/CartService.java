package group3.paws_hope.service;

import group3.paws_hope.dto.req.CartReq;
import group3.paws_hope.dto.res.CartRes;
import group3.paws_hope.entity.Cart;
import group3.paws_hope.entity.Product;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.CartRepository;
import group3.paws_hope.repository.ProductRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<CartRes> getByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId).stream()
                .map(CartRes::toJson)
                .toList();
    }

    public CartRes addToCart(CartReq req) {
        try {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!product.getIsActive()) {
                throw new RuntimeException("Product is not active");
            }

            if (req.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }

            Cart cart = cartRepository
                    .findByUser_UserIdAndProduct_ProductId(req.getUserId(), req.getProductId())
                    .orElse(null);

            if (cart != null) {
                int newQuantity = cart.getQuantity() + req.getQuantity();

                if (newQuantity > product.getStockQuantity()) {
                    throw new RuntimeException("Not enough stock");
                }

                cart.setQuantity(newQuantity);
            } else {
                if (req.getQuantity() > product.getStockQuantity()) {
                    throw new RuntimeException("Not enough stock");
                }

                cart = new Cart();
                cart.setUser(user);
                cart.setProduct(product);
                cart.setQuantity(req.getQuantity());
            }
            return CartRes.toJson(cartRepository.save(cart));
        } catch (Exception e) {
            return null;
        }
    }

    public CartRes updateQuantity(Long cartId, Integer quantity) {
        try {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));
            if (quantity <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }

            if (quantity > cart.getProduct().getStockQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            cart.setQuantity(quantity);

            return CartRes.toJson(cartRepository.save(cart));
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void clearCart(Long userId) {
        cartRepository.deleteByUser_UserId(userId);
    }
}