package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerApplicationReq;
import group3.paws_hope.dto.res.VolunteerApplicationRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.VolunteerApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_applications")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VolunteerApplicationController {
    private final VolunteerApplicationService volunteerApplicationService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<VolunteerApplicationRes>>> getAll() {
        return ResponseHandler.success(volunteerApplicationService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<VolunteerApplicationRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerApplicationService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<VolunteerApplicationRes>> create(@Valid @RequestBody VolunteerApplicationReq req) {
        VolunteerApplicationRes res = volunteerApplicationService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Application submitted successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Submit application failed");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<VolunteerApplicationRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String rejectionReason) {
        VolunteerApplicationRes res = volunteerApplicationService.updateStatus(id, status,reviewerId, rejectionReason);

        if (res != null) {
            return ResponseHandler.success(res, "Status update successful");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerApplicationService.delete(id);
        return ResponseHandler.success("Invoice deleted successfully.", "Success");
    }
}