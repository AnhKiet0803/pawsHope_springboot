package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerScheduleWeekReq;
import group3.paws_hope.dto.res.VolunteerScheduleWeekRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.VolunteerScheduleWeekService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_schedule_weeks")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VolunteerScheduleWeekController {

    private final VolunteerScheduleWeekService volunteerScheduleWeekService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<VolunteerScheduleWeekRes>>> getAll() {
        return ResponseHandler.success(volunteerScheduleWeekService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerScheduleWeekService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> create(
            @Valid @RequestBody VolunteerScheduleWeekReq req) {

        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule week created successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create schedule week failed");
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> submit(@PathVariable Long id) {
        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.submit(id);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule week submitted successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST,
                "Submit failed. Minimum 5 working days required.");
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> approve(
            @PathVariable Long id,
            @RequestParam Long approvedBy) {

        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.approve(id, approvedBy);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule week approved successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Approve failed");
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWeekRes>> reject(
            @PathVariable Long id,
            @RequestParam Long approvedBy,
            @RequestParam String reason) {

        VolunteerScheduleWeekRes res = volunteerScheduleWeekService.reject(id, approvedBy, reason);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule week rejected successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Reject failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerScheduleWeekService.delete(id);
        return ResponseHandler.success("Schedule week deleted successfully.", "Success");
    }
}