package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.OrderReq;
import group3.paws_hope.dto.res.OrderRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<OrderRes>>> getAll() {
        return ResponseHandler.success(orderService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<OrderRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(orderService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<OrderRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(orderService.getByUserId(userId), "Success");
    }

    @PostMapping("/checkout")
    public ResponseEntity<ResponseDTO<OrderRes>> createFromCart(@Valid @RequestBody OrderReq req) {
        OrderRes res = orderService.createFromCart(req);
        if (res != null) {
            return ResponseHandler.success(res, "Order created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create order failed");
    }

    @PatchMapping("/{id}/order-status")
    public ResponseEntity<ResponseDTO<OrderRes>> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        OrderRes res = orderService.updateOrderStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Order status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update order status failed");
    }

    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<ResponseDTO<OrderRes>> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        OrderRes res = orderService.updatePaymentStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Payment status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update payment status failed");
    }
}