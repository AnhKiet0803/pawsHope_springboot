package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.CreatePaypalOrderReq;
import group3.paws_hope.dto.req.OrderReq;
import group3.paws_hope.dto.res.OrderRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.OrderService;
import group3.paws_hope.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/paypal")
@RequiredArgsConstructor
public class PaypalController {
    private final PaypalService paypalService;
    private final OrderService orderService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> createOrder(
            @RequestBody CreatePaypalOrderReq req
    ) {
        return ResponseHandler.success(
                paypalService.createOrder(req.getAmountUsd()),
                "PayPal order created"
        );
    }

    @PostMapping("/capture-order/{paypalOrderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> captureOrder(
            @PathVariable String paypalOrderId,
            @RequestBody OrderReq req
    ) {
        try {
            Map<String, Object> captureResult = paypalService.captureOrder(paypalOrderId);
            System.out.println("CAPTURE RESULT = " + captureResult);

            String status = String.valueOf(captureResult.get("status"));

            if (!"COMPLETED".equals(status)) {
                return ResponseHandler.error(StatusCode.BAD_REQUEST, "PayPal payment not completed");
            }

            OrderRes order = orderService.createFromCart(req);

            if (order == null) {
                return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create order failed");
            }

            OrderRes paidOrder = orderService.updatePaymentStatus(order.getOrderId(), "PAID");

            return ResponseHandler.success(
                    paidOrder,
                    "PayPal payment completed and order created"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}