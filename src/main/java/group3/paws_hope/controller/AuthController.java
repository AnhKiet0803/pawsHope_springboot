package group3.paws_hope.controller;


import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.LoginUser;
import group3.paws_hope.dto.req.RegisterUser;
import group3.paws_hope.dto.res.LoginRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    public final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Boolean>> register(@RequestBody RegisterUser req){
        try {
            return ResponseHandler.success(authService.register(req),"Register successfully");
        }catch (Exception e){
            return ResponseHandler.error(StatusCode.BAD_REQUEST,"Register failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginRes>> login(@RequestBody LoginUser req){
        try {
            return ResponseHandler.success(authService.authenticate(req),"Login successfully");
        }catch (Exception e){
            return ResponseHandler.error(StatusCode.UNAUTHORIZED,e.getMessage());
        }
    }
}
