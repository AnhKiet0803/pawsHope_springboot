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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<CartRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(cartService.getByUserId(userId), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CartRes>> addToCart(@Valid @RequestBody CartReq req) {
        CartRes res = cartService.addToCart(req);

        if (res != null) {
            return ResponseHandler.success(res, "Product added to cart.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Add to cart failed");
    }

    @PatchMapping("/{cartId}/quantity")
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
    public ResponseEntity<ResponseDTO<String>> remove(@PathVariable Long cartId) {
        cartService.remove(cartId);
        return ResponseHandler.success("Cart item removed.", "Success");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<String>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseHandler.success("Cart cleared.", "Success");
    }
}