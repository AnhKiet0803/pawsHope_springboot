package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.res.EmailLogRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.EmailLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/email_logs")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailLogController {
    private final EmailLogService emailLogService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EmailLogRes>>> getAll(@RequestParam Long adminId) {
        try {
            return ResponseHandler.success(emailLogService.getAll(adminId), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EmailLogRes>> getById(@PathVariable Long id, @RequestParam Long adminId) {
        try {
            return ResponseHandler.success(emailLogService.findById(id, adminId), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}