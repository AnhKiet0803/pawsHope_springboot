package group3.paws_hope.repository;

import group3.paws_hope.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser_UserId(Long userId);
    Optional<Cart> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
    void deleteByUser_UserId(Long userId);
}