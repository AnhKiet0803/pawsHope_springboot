package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.VolunteerScheduleReq;
import group3.paws_hope.dto.res.VolunteerScheduleRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.VolunteerScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer_schedules")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VolunteerScheduleController {

    private final VolunteerScheduleService volunteerScheduleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<VolunteerScheduleRes>>> getAll() {
        return ResponseHandler.success(volunteerScheduleService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(volunteerScheduleService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VOLUNTEER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<VolunteerScheduleRes>> create(@Valid @RequestBody VolunteerScheduleReq req) {
        VolunteerScheduleRes res = volunteerScheduleService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Schedule registered successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Register schedule failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        volunteerScheduleService.delete(id);
        return ResponseHandler.success("Schedule deleted successfully.", "Success");
    }
}