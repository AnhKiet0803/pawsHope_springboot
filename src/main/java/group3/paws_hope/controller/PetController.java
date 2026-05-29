package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.PetReq;
import group3.paws_hope.dto.req.PetStatusLogReq;
import group3.paws_hope.dto.res.PetRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.PetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@AllArgsConstructor
public class PetController {
    private final PetService petService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<PetRes>>> getAll() {
        return ResponseHandler.success(petService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PetRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(petService.getById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/code/{petCode}")
    public ResponseEntity<ResponseDTO<PetRes>> getByPetCode(@PathVariable String petCode) {
        try {
            return ResponseHandler.success(petService.getByPetCode(petCode), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetRes>> create(@Valid @RequestBody PetReq req) {
        PetRes res = petService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Pet created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create pet failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetRes>> update(@PathVariable Long id, @Valid @RequestBody PetReq req) {
        PetRes res = petService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Pet updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update pet failed");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<PetRes>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody PetStatusLogReq req) {
        PetRes res = petService.updateStatus(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Pet status updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update pet status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseHandler.success("Pet deleted successfully.", "Success");
    }
}