package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.NotificationReq;
import group3.paws_hope.dto.res.NotificationRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<NotificationRes>>> getAll() {
        return ResponseHandler.success(notificationService.getAll(), "Success");
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<NotificationRes>>> getByUserId(
            @PathVariable Long userId) {

        return ResponseHandler.success(notificationService.getByUserId(userId), "Success");
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<List<NotificationRes>>> getUnreadByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(notificationService.getUnreadByUserId(userId), "Success");
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<ResponseDTO<Long>> countUnread(@PathVariable Long userId) {
        return ResponseHandler.success(notificationService.countUnread(userId), "Success");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    public ResponseEntity<ResponseDTO<NotificationRes>> create(@Valid @RequestBody NotificationReq req) {
        NotificationRes res = notificationService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Notification created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create notification failed");
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<NotificationRes>> markAsRead(@PathVariable Long id) {

        NotificationRes res = notificationService.markAsRead(id);
        if (res != null) {
            return ResponseHandler.success(res, "Notification marked as read.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Mark notification as read failed");
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ResponseDTO<String>> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);

        return ResponseHandler.success("All notifications marked as read.", "Success");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseHandler.success("Notification deleted successfully.", "Success");
    }
}