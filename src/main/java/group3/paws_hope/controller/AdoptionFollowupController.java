package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionFollowupReq;
import group3.paws_hope.dto.res.AdoptionFollowupRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AdoptionFollowupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption_followups")
@AllArgsConstructor
public class AdoptionFollowupController {

    private final AdoptionFollowupService adoptionFollowupService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<AdoptionFollowupRes>>> getAll() {
        return ResponseHandler.success(adoptionFollowupService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByFollowupId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionFollowupRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionFollowupService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/adoption/{adoptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByAdoptionId(#adoptionId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<AdoptionFollowupRes>>> getByAdoptionId(
            @PathVariable Long adoptionId
    ) {
        try {
            return ResponseHandler.success(
                    adoptionFollowupService.getByAdoptionId(adoptionId),
                    "Success"
            );
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionFollowupRes>> create(
            @Valid @RequestBody AdoptionFollowupReq req
    ) {

        AdoptionFollowupRes res = adoptionFollowupService.create(req);

        if (res != null) {
            return ResponseHandler.success(res, "Follow-up created successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create follow-up failed");
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionFollowupRes>> confirm(@PathVariable Long id) {

        AdoptionFollowupRes res = adoptionFollowupService.confirm(id);

        if (res != null) {
            return ResponseHandler.success(res, "Follow-up confirmed successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Confirm follow-up failed");
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionFollowupRes>> complete(
            @PathVariable Long id,
            @RequestBody AdoptionFollowupReq req
    ) {

        AdoptionFollowupRes res = adoptionFollowupService.complete(id, req);

        if (res != null) {
            return ResponseHandler.success(res, "Follow-up completed successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Complete follow-up failed");
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionFollowupRes>> cancel(@PathVariable Long id) {

        AdoptionFollowupRes res = adoptionFollowupService.cancel(id);

        if (res != null) {
            return ResponseHandler.success(res, "Follow-up cancelled successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Cancel follow-up failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionFollowupService.delete(id);

        return ResponseHandler.success(
                "Follow-up deleted successfully.",
                "Success"
        );
    }
}