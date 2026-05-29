package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ShiftReq;
import group3.paws_hope.dto.res.ShiftRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ShiftService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<List<ShiftRes>>> getAll() {
        try {
            return ResponseHandler.success(shiftService.getAllShifts(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<ShiftRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(shiftService.findById(id),"Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ShiftRes>> create(@Valid @RequestBody ShiftReq req) {
        ShiftRes res = shiftService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Success");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Another failed shift");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ShiftRes>> update(@PathVariable Long id, @Valid @RequestBody ShiftReq req) {
        ShiftRes res = shiftService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Updated successfully");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update failed");
    }

}