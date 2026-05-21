package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionMeetingReq;
import group3.paws_hope.dto.res.AdoptionMeetingRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AdoptionMeetingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption_meetings")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AdoptionMeetingController {

    private final AdoptionMeetingService adoptionMeetingService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AdoptionMeetingRes>>> getAll() {
        return ResponseHandler.success(adoptionMeetingService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionMeetingService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/adoption/{adoptionId}")
    public ResponseEntity<ResponseDTO<List<AdoptionMeetingRes>>> getByAdoptionId(@PathVariable Long adoptionId) {
        return ResponseHandler.success(adoptionMeetingService.getByAdoptionId(adoptionId), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> create(@Valid @RequestBody AdoptionMeetingReq req) {
        AdoptionMeetingRes res = adoptionMeetingService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Meeting scheduled successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Schedule meeting failed");
    }

    @PatchMapping("/{id}/result")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> updateResult(
            @PathVariable Long id, @RequestParam String result, @RequestParam(required = false) String note) {

        AdoptionMeetingRes res = adoptionMeetingService.updateResult(id, result, note);

        if (res != null) {
            return ResponseHandler.success(res, "Meeting result updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update result failed");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        AdoptionMeetingRes res = adoptionMeetingService.updateStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Meeting status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionMeetingService.delete(id);
        return ResponseHandler.success("Meeting deleted successfully.", "Success");
    }
}