package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionReq;
import group3.paws_hope.dto.res.AdoptionRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AdoptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoptions")
@AllArgsConstructor
public class AdoptionController {
    private final AdoptionService adoptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<AdoptionRes>>> getAll() {
        return ResponseHandler.success(adoptionService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByAdoptionId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByApplicationCode(#code, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionRes>> getByApplicationCode(@PathVariable String code) {
        try {
            return ResponseHandler.success(adoptionService.getByApplicationCode(code), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<AdoptionRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(adoptionService.getByUserId(userId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ResponseDTO<AdoptionRes>> create(@Valid @RequestBody AdoptionReq req) {
        AdoptionRes res = adoptionService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Adoption application submitted successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Submit adoption application failed");
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionRes>> approve(@PathVariable Long id, @RequestParam Long processedBy) {
        AdoptionRes res = adoptionService.approve(id, processedBy);
        if (res != null) {
            return ResponseHandler.success(res, "Adoption application approved successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Approve failed");
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionRes>> reject(
            @PathVariable Long id,@RequestParam Long processedBy,@RequestParam(required = false) String note) {
        AdoptionRes res = adoptionService.reject(id, processedBy, note);
        if (res != null) {
            return ResponseHandler.success(res, "Adoption application rejected successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Reject failed");
    }

    @PatchMapping("/{id}/payment-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionRes>> updatePaymentStatus(
            @PathVariable Long id, @RequestParam String paymentStatus) {
        AdoptionRes res = adoptionService.updatePaymentStatus(id, paymentStatus);
        if (res != null) {
            return ResponseHandler.success(res, "Payment status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update payment status failed");
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<AdoptionRes>> complete(@PathVariable Long id) {
        AdoptionRes res = adoptionService.complete(id);
        if (res != null) {
            return ResponseHandler.success(res, "Adoption completed successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Complete adoption failed");
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER') or @adoptionSecurity.isOwnerByAdoptionId(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<AdoptionRes>> cancel(@PathVariable Long id) {
        AdoptionRes res = adoptionService.cancel(id);
        if (res != null) {
            return ResponseHandler.success(res, "Adoption cancelled successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Cancel adoption failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        adoptionService.delete(id);
        return ResponseHandler.success("Adoption deleted successfully.", "Success");
    }
}