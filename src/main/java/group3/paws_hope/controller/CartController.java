package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.CartReq;
import group3.paws_hope.dto.res.CartRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<CartRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(cartService.getByUserId(userId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO<CartRes>> addToCart(
            @Valid @RequestBody CartReq req,
            Authentication authentication
    ) {
        CartRes res = cartService.addToCart(req, authentication.getName());

        return ResponseHandler.success(res, "Product added to cart.");
    }

    @PatchMapping("/{cartId}/quantity")
    @PreAuthorize("@cartSecurity.isOwner(#cartId, authentication.name)")
    public ResponseEntity<ResponseDTO<CartRes>> updateQuantity(
            @PathVariable Long cartId,
            @RequestParam Integer quantity) {

        CartRes res = cartService.updateQuantity(cartId, quantity);

        if (res != null) {
            return ResponseHandler.success(res, "Cart quantity updated.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update cart quantity failed");
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("@cartSecurity.isOwner(#cartId, authentication.name)")
    public ResponseEntity<ResponseDTO<String>> remove(@PathVariable Long cartId) {
        cartService.remove(cartId);
        return ResponseHandler.success("Cart item removed.", "Success");
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<String>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseHandler.success("Cart cleared.", "Success");
    }
}