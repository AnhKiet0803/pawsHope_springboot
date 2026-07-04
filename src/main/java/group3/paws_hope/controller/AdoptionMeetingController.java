package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionMeetingReq;
import group3.paws_hope.dto.res.AdoptionMeetingRes;
import group3.paws_hope.entity.AdoptionMeeting;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.repository.AdoptionMeetingRepository;
import group3.paws_hope.service.AdoptionMeetingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/adoption_meetings")
@AllArgsConstructor
public class AdoptionMeetingController {
    private final AdoptionMeetingService adoptionMeetingService;
    private final AdoptionMeetingRepository adoptionMeetingRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<AdoptionMeetingRes>>> getAll() {
        return ResponseHandler.success(adoptionMeetingService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByMeetingId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionMeetingService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/adoption/{adoptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByAdoptionId(#adoptionId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<AdoptionMeetingRes>>> getByAdoptionId(@PathVariable Long adoptionId) {
        return ResponseHandler.success(adoptionMeetingService.getByAdoptionId(adoptionId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> create(@Valid @RequestBody AdoptionMeetingReq req) {
        AdoptionMeetingRes res = adoptionMeetingService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Meeting scheduled successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Schedule meeting failed");
    }

    @PatchMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> updateResult(
            @PathVariable Long id, @RequestParam String result, @RequestParam(required = false) String note) {

        AdoptionMeetingRes res = adoptionMeetingService.updateResult(id, result, note);
        if (res != null) {
            return ResponseHandler.success(res, "Meeting result updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update result failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {

        AdoptionMeetingRes res = adoptionMeetingService.updateStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Meeting status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionMeetingService.delete(id);
        return ResponseHandler.success("Meeting deleted successfully.", "Success");
    }

    @PatchMapping("/{id}/reschedule_request")
    public ResponseEntity<ResponseDTO<AdoptionMeetingRes>> requestReschedule(
            @PathVariable Long id, @RequestBody String proposedSlots) {

        AdoptionMeetingRes res = adoptionMeetingService.requestReschedule(id, proposedSlots);
        if (res != null) {
            return ResponseHandler.success(res, "Reschedule request submitted successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Failed to submit reschedule request.");
    }

    @PatchMapping("/{id}/reschedule-confirm")
    public ResponseEntity<ResponseDTO<String>> confirmReschedule(
            @PathVariable Long id, @RequestBody Map<String, String> body) {

        String newDatetime = body.get("newDatetime");
        AdoptionMeeting meeting = adoptionMeetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setMeetingDatetime(LocalDateTime.parse(newDatetime));
        meeting.setStatus(AdoptionMeeting.Status.SCHEDULED);
        meeting.setNote("Rescheduled officially to: " + newDatetime);

        adoptionMeetingRepository.save(meeting);

        return ResponseHandler.success("Confirmed", "Meeting update successfully");
    }

    @PatchMapping("/{id}/confirm")
    //@PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByMeetingId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<String>> confirmMeetingAttendance(@PathVariable Long id) {
        try {
            AdoptionMeeting meeting = adoptionMeetingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + id));

            meeting.setStatus(AdoptionMeeting.Status.CONFIRMED);

            String currentNote = meeting.getNote() != null ? meeting.getNote() : "";
            meeting.setNote(currentNote + "\n[System] Customer confirmed attendance at: " + LocalDateTime.now());

            adoptionMeetingRepository.save(meeting);

            return ResponseHandler.success("Confirmed", "Attendance confirmed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.error(StatusCode.BAD_REQUEST, "Error confirming attendance: " + e.getMessage());
        }
    }
}