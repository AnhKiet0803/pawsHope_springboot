package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.res.RescueReportRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.service.RescueReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rescue_reports")
@AllArgsConstructor
public class RescueReportController {

    private final RescueReportService rescueReportService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<RescueReportRes>>> getAll() {
        return ResponseHandler.success(rescueReportService.getAll(), "Success");
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDTO<List<RescueReportRes>>> getMyReports(Authentication authentication) {
        try {
            Long userId = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUserId();
            return ResponseHandler.success(rescueReportService.getByUserId(userId), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<RescueReportRes>> create(
            @RequestParam(required = false) Long userId,
            @RequestParam String reporterName,
            @RequestParam String reporterPhone,
            @RequestParam String locationText,
            @RequestParam(defaultValue = "MEDIUM") String urgencyLevel,
            @RequestParam(defaultValue = "NONE") String injuryType,
            @RequestParam(defaultValue = "SCARED") String temperament,
            @RequestParam(defaultValue = "ACTIVE") String behavior,
            @RequestParam String additionalNote,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            group3.paws_hope.dto.req.RescueReportReq req = new group3.paws_hope.dto.req.RescueReportReq();
            req.setUserId(userId);
            req.setReporterName(reporterName);
            req.setReporterPhone(reporterPhone);
            req.setLocationText(locationText);
            req.setUrgencyLevel(urgencyLevel);
            req.setInjuryType(injuryType);
            req.setTemperament(temperament);
            req.setBehavior(behavior);
            req.setAdditionalNote(additionalNote);

            RescueReportRes res = rescueReportService.create(req, image);
            broadcastRescueUpdate(res);
            return ResponseHandler.success(res, "Rescue report submitted successfully.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<RescueReportRes>> accept(@PathVariable Long id, Authentication authentication) {
        try {
            RescueReportRes res = rescueReportService.accept(id, authentication.getName());
            broadcastRescueUpdate(res);
            return ResponseHandler.success(res, "Rescue report accepted successfully.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<RescueReportRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            RescueReportRes res = rescueReportService.updateStatus(id, status);
            broadcastRescueUpdate(res);
            return ResponseHandler.success(res, "Status updated successfully.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        rescueReportService.delete(id);
        messagingTemplate.convertAndSend("/topic/rescue/admin", "RESCUE_DELETED");
        return ResponseHandler.success("Rescue report deleted successfully.", "Success");
    }

    private void broadcastRescueUpdate(RescueReportRes res) {
        if (res == null || res.getTrackingCode() == null) return;
        messagingTemplate.convertAndSend("/topic/rescue/" + res.getTrackingCode(), "RESCUE_UPDATED");
        messagingTemplate.convertAndSend("/topic/rescue/admin", "RESCUE_UPDATED");
    }
}
