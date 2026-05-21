package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.KennelReq;
import group3.paws_hope.dto.res.KennelRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.KennelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kennels")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class KennelController {
    private final KennelService kennelService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<KennelRes>>> getAll() {
        return ResponseHandler.success(kennelService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<KennelRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(kennelService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST,e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<KennelRes>> create(@Valid @RequestBody KennelReq req) {
        KennelRes res = kennelService.create(req);
        return res != null ? ResponseHandler.success(res, "Success")
                : ResponseHandler.error(StatusCode.BAD_REQUEST, "More failure");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<KennelRes>> update(@PathVariable Long id, @Valid @RequestBody KennelReq req) {
        KennelRes res = kennelService.update(id, req);
        return res != null ? ResponseHandler.success(res, "Updated successfully")
                : ResponseHandler.error(StatusCode.BAD_REQUEST, "Update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        kennelService.delete(id);

        return ResponseHandler.success("Kennel deleted successfully.", "Success");
    }
}