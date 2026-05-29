package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.UserReq;
import group3.paws_hope.dto.res.UserRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserRes>>> getAll() {
        try {
            return ResponseHandler.success(userService.getAllUsers(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<UserRes>> getById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(userService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<UserRes>> create(@Valid @RequestBody UserReq req) {
        UserRes res = userService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "User created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create user failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ResponseDTO<UserRes>> update(@PathVariable Long id, @Valid @RequestBody UserReq req) {
        UserRes res = userService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "User updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update failed");
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<UserRes>> updateRole(@PathVariable Long id, @RequestParam String role) {
        UserRes res = userService.updateRole(id, role);
        if (res != null) {
            return ResponseHandler.success(res, "Role update successful");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update role failed");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<UserRes>> updateStatus(@PathVariable Long id,@RequestParam Boolean status) {
        UserRes res = userService.updateStatus(id, status);
        if (res != null) {
            return ResponseHandler.success(res, "Status update successful");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseHandler.success("User deletion successful.", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}