package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerScheduleWeekReq;
import group3.paws_hope.dto.res.VolunteerScheduleWeekRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.entity.User;
import group3.paws_hope.service.VolunteerScheduleWeekService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_schedule_weeks")
@AllArgsConstructor
public class VolunteerScheduleWeekController {

    private final VolunteerScheduleWeekService volunteerScheduleWeekService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<VolunteerScheduleWeekRes>>> getAll(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseHandler.success(volunteerScheduleWeekService.getAll(), "Success");
        }

        String loginName = authentication.getName();

        // Thử tìm theo Email trước
        User currentUser = userRepository.findByEmail(loginName).orElse(null);

        if (currentUser == null) {
            return ResponseHandler.success(new java.util.ArrayList<>(), "User not found in session");
        }

        return ResponseHandler.success(volunteerScheduleWeekService.getByUserId(currentUser.getUserId()), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerScheduleWeekService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VOLUNTEER','ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> create(
            @Valid @RequestBody VolunteerScheduleWeekReq req) {
        try {
            VolunteerScheduleWeekRes res = volunteerScheduleWeekService.create(req);
            if (res != null) {
                return ResponseHandler.success(res, "Schedule week created successfully.");
            }
            return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create schedule week failed");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('VOLUNTEER','ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> submit(@PathVariable Long id) {
        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.submit(id);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule week submitted successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST,
                "Submit failed. Minimum 5 working days required.");
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> approve(
            @PathVariable Long id,
            Authentication authentication) {

        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.approve(id, authentication.getName());
        if (res != null) {
            return ResponseHandler.success(res, "Schedule week approved successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Approve failed");
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> reject(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body,
            Authentication authentication) {

        String reason = body.get("rejectionReason");
        if (reason == null || reason.trim().isEmpty()) {
            reason = "No reason provided";
        }

        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.reject(id, authentication.getName(), reason);
        if (res != null) {
            return ResponseHandler.success(res, "Schedule week rejected successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Reject failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerScheduleWeekService.delete(id);
        return ResponseHandler.success("Schedule week deleted successfully.", "Success");
    }
}