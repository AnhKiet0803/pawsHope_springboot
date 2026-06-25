package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionHandoverReq;
import group3.paws_hope.dto.res.AdoptionHandoverRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AdoptionHandoverService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption_handovers")
@AllArgsConstructor
public class AdoptionHandoverController {
    private final AdoptionHandoverService adoptionHandoverService;
    private final SimpMessagingTemplate messagingTemplate;

    // 🌟 HÀM DUYỆT TRẠNG THÁI: Đã dọn dẹp trùng lặp, tích hợp WebSocket + sẵn sàng gửi Email
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {

        AdoptionHandoverRes res = adoptionHandoverService.updateStatus(id, status);
        if (res != null) {
            // 1. 🌟 BẮN TÍN HIỆU REAL-TIME: Khách đổi giao diện hiển thị ngay lập tức không cần F5
            messagingTemplate.convertAndSend("/topic/adoption/" + res.getAdoptionId(), "HANDOVER_UPDATED");

            // 2. 🌟 TRIGGER GỬI EMAIL: Nếu Admin duyệt đổi lịch thành công (CONFIRMED)
            if ("CONFIRMED".equalsIgnoreCase(status)) {
                try {
                    // Bạn gọi hàm gửi email từ EmailService của bạn tại đây
                    // Ví dụ: emailService.sendHandoverConfirmationEmail(res);
                } catch (Exception e) {
                    System.err.println("Gặp lỗi khi gửi email thông báo: " + e.getMessage());
                }
            }

            return ResponseHandler.success(res, "Handover status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<AdoptionHandoverRes>>> getAll() {
        return ResponseHandler.success(adoptionHandoverService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByHandoverId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionHandoverService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/adoption/{adoptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByAdoptionId(#adoptionId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<AdoptionHandoverRes>>> getByAdoptionId(@PathVariable Long adoptionId) {
        return ResponseHandler.success(adoptionHandoverService.getByAdoptionId(adoptionId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> create(
            @Valid @RequestBody AdoptionHandoverReq req,
            BindingResult bindingResult) {

        // 1. Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseHandler.error(StatusCode.BAD_REQUEST, errorMsg);
        }

        // 2. Gọi service
        AdoptionHandoverRes res = adoptionHandoverService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Handover scheduled successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Schedule handover failed");
    }

    @PatchMapping("/{id}/confirm-adopter")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByHandoverId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> confirmAdopter(@PathVariable Long id) {

        AdoptionHandoverRes res = adoptionHandoverService.confirm(id);

        if (res != null) {
            return ResponseHandler.success(res, "Handover schedule confirmed by adopter successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Confirm handover failed");
    }

    @PatchMapping(value = "/{id}/complete", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> complete(
            @PathVariable Long id,
            @RequestParam(required = false) String completionNote,
            @RequestParam("file") MultipartFile file) {

        try {
            AdoptionHandoverRes res = adoptionHandoverService.complete(id, completionNote, file);

            if (res != null) {
                return ResponseHandler.success(res, "Handover completed successfully.");
            }
            return ResponseHandler.error(StatusCode.BAD_REQUEST, "Complete handover failed");

        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, "Upload or Save failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionHandoverService.delete(id);
        return ResponseHandler.success("Handover deleted successfully.", "Success");
    }

    @PatchMapping("/{id}/reschedule-request")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByHandoverId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> requestReschedule(
            @PathVariable Long id,
            @RequestParam("note") String note) {

        AdoptionHandoverRes res = adoptionHandoverService.requestReschedule(id, note);
        if (res != null) {
            return ResponseHandler.success(res, "Reschedule request sent successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Failed to send reschedule request.");
    }
}