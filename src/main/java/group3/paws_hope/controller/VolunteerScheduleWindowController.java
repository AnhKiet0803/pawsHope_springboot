package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerScheduleWindowReq;
import group3.paws_hope.dto.res.VolunteerScheduleWindowRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.VolunteerScheduleWindowService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_schedule_windows")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VolunteerScheduleWindowController {
    private final VolunteerScheduleWindowService volunteerScheduleWindowService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<VolunteerScheduleWindowRes>>> getAll() {
        return ResponseHandler.success(volunteerScheduleWindowService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWindowRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerScheduleWindowService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<VolunteerScheduleWindowRes>> create(
            @Valid @RequestBody VolunteerScheduleWindowReq req) {

        VolunteerScheduleWindowRes res = volunteerScheduleWindowService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule window created successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create schedule window failed");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWindowRes>> update(
            @PathVariable Long id,
            @Valid @RequestBody VolunteerScheduleWindowReq req) {

        VolunteerScheduleWindowRes res = volunteerScheduleWindowService.update(id, req);

        if (res != null) {
            return ResponseHandler.success(res, "Schedule window updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update schedule window failed");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<VolunteerScheduleWindowRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        VolunteerScheduleWindowRes res = volunteerScheduleWindowService.updateStatus(id, status);

        if (res != null) {
            return ResponseHandler.success(res, "Status updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerScheduleWindowService.delete(id);
        return ResponseHandler.success("Schedule window deleted successfully.", "Success");
    }
}