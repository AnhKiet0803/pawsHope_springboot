package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.DonationCampaignReq;
import group3.paws_hope.dto.res.DonationCampaignRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.DonationCampaignService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donation_campaigns")
@AllArgsConstructor
public class DonationCampaignController {
    private final DonationCampaignService donationCampaignService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DonationCampaignRes>>> getAll() {
        return ResponseHandler.success(donationCampaignService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DonationCampaignRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(donationCampaignService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO<List<DonationCampaignRes>>> getByStatus(@PathVariable String status) {
        return ResponseHandler.success(donationCampaignService.getByStatus(status), "Success");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<DonationCampaignRes>> create(
            @Valid @RequestBody DonationCampaignReq req, Authentication authentication) {

        DonationCampaignRes res = donationCampaignService.create(req, authentication.getName());
        return ResponseHandler.success(res, "Campaign created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<DonationCampaignRes>> update(
            @PathVariable Long id, @Valid @RequestBody DonationCampaignReq req) {

        DonationCampaignRes res = donationCampaignService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Campaign updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update campaign failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<DonationCampaignRes>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {

        DonationCampaignRes res = donationCampaignService.updateStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Campaign status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update campaign status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        donationCampaignService.delete(id);
        return ResponseHandler.success("Campaign deleted successfully.", "Success");
    }
}