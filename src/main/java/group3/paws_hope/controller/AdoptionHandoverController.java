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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption_handovers")
@AllArgsConstructor
public class AdoptionHandoverController {
    private final AdoptionHandoverService adoptionHandoverService;

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
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> create(@Valid @RequestBody AdoptionHandoverReq req) {
        AdoptionHandoverRes res = adoptionHandoverService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Handover scheduled successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Schedule handover failed");
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> confirm(@PathVariable Long id) {
        AdoptionHandoverRes res = adoptionHandoverService.confirm(id);
        if (res != null) {
            return ResponseHandler.success(res, "Handover confirmed successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Confirm handover failed");
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> complete(
            @PathVariable Long id, @RequestParam(required = false) String completionNote) {

        AdoptionHandoverRes res = adoptionHandoverService.complete(id, completionNote);
        if (res != null) {
            return ResponseHandler.success(res, "Handover completed successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Complete handover failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionHandoverRes>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {

        AdoptionHandoverRes res = adoptionHandoverService.updateStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Handover status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionHandoverService.delete(id);
        return ResponseHandler.success("Handover deleted successfully.", "Success");
    }
}