package group3.paws_hope.controller;



import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.AdoptionGuidelineReq;
import group3.paws_hope.dto.res.AdoptionGuidelineRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AdoptionGuidelineService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption_guidelines")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AdoptionGuidelineController {
    private final AdoptionGuidelineService adoptionGuidelineService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<AdoptionGuidelineRes>>> getAll() {
        try {
            return ResponseHandler.success(adoptionGuidelineService.getAll(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AdoptionGuidelineRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(adoptionGuidelineService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<AdoptionGuidelineRes>> create(@Valid @RequestBody AdoptionGuidelineReq req) {
        try {
            return ResponseHandler.success(adoptionGuidelineService.create(req), "Success");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AdoptionGuidelineRes>> update(@PathVariable Long id, @Valid @RequestBody AdoptionGuidelineReq req) {
        try {
            return ResponseHandler.success(adoptionGuidelineService.update(id, req), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        try {
            adoptionGuidelineService.delete(id);
            return ResponseHandler.success("Deleted successfully", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}