package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.LoginReq;
import group3.paws_hope.dto.req.RegisterUser;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AuthService;
import group3.paws_hope.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Boolean>> register(@RequestBody RegisterUser req) {
        try {
            return ResponseHandler.success(authService.register(req), "Register successfully");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, "Register failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> login(@RequestBody LoginReq req) {
        try {
            Map<String, Object> response = userService.login(req);
            return ResponseHandler.success(response, "Login successful");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}