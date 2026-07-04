package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.CreatePaypalOrderReq;
import group3.paws_hope.dto.req.OrderReq;
import group3.paws_hope.dto.res.OrderRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.OrderService;
import group3.paws_hope.service.PaypalService;
import group3.paws_hope.service.DonationService;
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
    private final DonationService donationService;

    @PostMapping("/create-order")
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
    public ResponseEntity<?> captureOrder(@PathVariable String paypalOrderId, @RequestBody OrderReq req) {
        try {
            Map<String, Object> captureResult = paypalService.captureOrder(paypalOrderId);

            String status = String.valueOf(captureResult.get("status"));

            if (!"COMPLETED".equals(status)) {
                return ResponseHandler.error(StatusCode.BAD_REQUEST, "PayPal payment not completed");
            }

            OrderRes paidOrder = orderService.finishOrder(req.getOrderId());

            return ResponseHandler.success(
                    paidOrder,
                    "PayPal payment completed"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/capture-donation/{paypalOrderId}")
    public ResponseEntity<?> captureDonation(
            @PathVariable String paypalOrderId,
            @RequestBody Map<String, Object> payload
    ) {
        try {
            Map<String, Object> captureResult = paypalService.captureOrder(paypalOrderId);
            String status = String.valueOf(captureResult.get("status"));

            if (!"COMPLETED".equals(status)) {
                return ResponseHandler.error(StatusCode.BAD_REQUEST, "PayPal payment for donation not completed");
            }

            Long campaignId = Long.valueOf(payload.get("campaignId").toString());

            Long userId = null;
            if (payload.get("userId") != null && !payload.get("userId").toString().equals("null")) {
                userId = Long.valueOf(payload.get("userId").toString());
            }

            String donorNameManual = payload.get("donorNameManual") != null ? payload.get("donorNameManual").toString() : "Guest";
            Double amount = Double.valueOf(payload.get("amount").toString());

            donationService.createDonationFromPaypal(campaignId, userId, donorNameManual, amount);

            return ResponseHandler.success(captureResult, "PayPal donation completed and recorded!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing PayPal donation: " + e.getMessage());
        }
    }
}