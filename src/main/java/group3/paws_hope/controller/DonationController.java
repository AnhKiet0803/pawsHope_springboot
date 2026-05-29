package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.DonationReq;
import group3.paws_hope.dto.res.DonationRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.DonationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@AllArgsConstructor
public class DonationController {
    private final DonationService donationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<DonationRes>>> getAll() {
        return ResponseHandler.success(donationService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTEER')")
    public ResponseEntity<ResponseDTO<DonationRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(donationService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/campaign/{campaignId}")
    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<DonationRes>>> getByCampaignId(@PathVariable Long campaignId) {
        return ResponseHandler.success(donationService.getByCampaignId(campaignId), "Success");
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTEER') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<DonationRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(donationService.getByUserId(userId), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<DonationRes>> create(@Valid @RequestBody DonationReq req) {
        DonationRes res = donationService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Donation created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create donation failed");
    }

    @PatchMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<DonationRes>> updatePaymentStatus(
            @PathVariable Long id, @RequestParam String status) {

        DonationRes res = donationService.updatePaymentStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Payment status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update payment status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        donationService.delete(id);
        return ResponseHandler.success("Donation deleted successfully.", "Success");
    }
}