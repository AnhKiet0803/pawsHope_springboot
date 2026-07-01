package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ContactMessageReq;
import group3.paws_hope.dto.res.ContactMessageRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contact_messages")
@AllArgsConstructor
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<ContactMessageRes>>> getAll() {
        return ResponseHandler.success(contactMessageService.getAll(), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ContactMessageRes>> create(@Valid @RequestBody ContactMessageReq req) {
        try {
            return ResponseHandler.success(contactMessageService.create(req), "Message sent successfully.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ContactMessageRes>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseHandler.success(contactMessageService.updateStatus(id, status), "Status updated.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}
