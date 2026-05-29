package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.PetMedicalRecordReq;
import group3.paws_hope.dto.res.PetMedicalRecordRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.PetMedicalRecordService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet_medical_records")
@AllArgsConstructor
public class PetMedicalRecordController {

    private final PetMedicalRecordService petMedicalRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<PetMedicalRecordRes>>> getAll() {
        return ResponseHandler.success(petMedicalRecordService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetMedicalRecordRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(petMedicalRecordService.getById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<PetMedicalRecordRes>>> getByPetId(@PathVariable Long petId) {
        return ResponseHandler.success(petMedicalRecordService.getByPetId(petId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetMedicalRecordRes>> create(@Valid @RequestBody PetMedicalRecordReq req) {
        PetMedicalRecordRes res = petMedicalRecordService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Medical record created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create medical record failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetMedicalRecordRes>> update(
            @PathVariable Long id, @Valid @RequestBody PetMedicalRecordReq req) {

        PetMedicalRecordRes res = petMedicalRecordService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Medical record updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update medical record failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        petMedicalRecordService.delete(id);
        return ResponseHandler.success("Medical record deleted successfully.", "Success");
    }
}