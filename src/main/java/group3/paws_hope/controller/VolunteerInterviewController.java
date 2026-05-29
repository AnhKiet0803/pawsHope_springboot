package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerInterviewReq;
import group3.paws_hope.dto.res.VolunteerInterviewRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.VolunteerInterviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_interviews")
@AllArgsConstructor
public class VolunteerInterviewController {
    private final VolunteerInterviewService volunteerInterviewService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<VolunteerInterviewRes>>> getAll() {
        return ResponseHandler.success(volunteerInterviewService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerInterviewRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerInterviewService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerInterviewRes>> create(
            @Valid @RequestBody VolunteerInterviewReq req) {

        VolunteerInterviewRes res = volunteerInterviewService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Interview scheduled successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Schedule interview failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerInterviewRes>> update(
            @PathVariable Long id,
            @Valid @RequestBody VolunteerInterviewReq req) {

        VolunteerInterviewRes res = volunteerInterviewService.update(id, req);

        if (res != null) {
            return ResponseHandler.success(res, "Interview updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update interview failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerInterviewRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        VolunteerInterviewRes res = volunteerInterviewService.updateStatus(id, status);

        if (res != null) {
            return ResponseHandler.success(res, "Status updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @PatchMapping("/{id}/result")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerInterviewRes>> updateResult(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam(required = false) String evaluationNote) {

        VolunteerInterviewRes res = volunteerInterviewService.updateResult(id, result, evaluationNote);

        if (res != null) {
            return ResponseHandler.success(res, "Result updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update result failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerInterviewService.delete(id);
        return ResponseHandler.success("Interview deleted successfully.", "Success");
    }
}