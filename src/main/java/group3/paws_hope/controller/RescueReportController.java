package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.RescueReportReq;
import group3.paws_hope.dto.res.RescueReportRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.RescueReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rescue_reports")
@AllArgsConstructor
public class RescueReportController {

    private final RescueReportService rescueReportService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<RescueReportRes>>> getAll() {
        return ResponseHandler.success(rescueReportService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<RescueReportRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(rescueReportService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<ResponseDTO<RescueReportRes>> getByTrackingCode(@PathVariable String trackingCode) {
        try {
            return ResponseHandler.success(rescueReportService.getByTrackingCode(trackingCode), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<RescueReportRes>> create(@Valid @RequestBody RescueReportReq req) {
        RescueReportRes res = rescueReportService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Rescue report submitted successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Submit rescue report failed");
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<RescueReportRes>> accept(@PathVariable Long id,Authentication authentication) {
        RescueReportRes res = rescueReportService.accept(id, authentication.getName());
        if (res != null) {
            return ResponseHandler.success(res, "Rescue report accepted successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Accept rescue report failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<RescueReportRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        RescueReportRes res = rescueReportService.updateStatus(id, status);

        if (res != null) {
            return ResponseHandler.success(res, "Status updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        rescueReportService.delete(id);
        return ResponseHandler.success("Rescue report deleted successfully.", "Success");
    }
}