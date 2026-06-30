package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ContactMessageReq;
import group3.paws_hope.dto.res.ContactMessageRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ContactMessageService;
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
        return ResponseHandler.success(
                contactMessageService.getAllMessages(),
                "Success"
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ContactMessageRes>> getById(@PathVariable Long id) {

        return ResponseHandler.success(contactMessageService.getMessageById(id), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create(@RequestBody ContactMessageReq req) {
        boolean ok = contactMessageService.createMessage(req);

        if(ok){
            return ResponseHandler.success("Message sent successfully", "Success");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Send failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ContactMessageRes>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseHandler.success(contactMessageService.updateStatus(id, status), "Updated");
    }
}