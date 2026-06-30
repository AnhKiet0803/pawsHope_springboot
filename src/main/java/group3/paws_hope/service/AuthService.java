package group3.paws_hope.service;

import group3.paws_hope.dto.req.LoginUser;
import group3.paws_hope.dto.req.RegisterUser;
import group3.paws_hope.dto.res.LoginRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public boolean register(RegisterUser input) {
        String cleanEmail = input.getEmail() != null ? input.getEmail().trim().toLowerCase() : "";
        String cleanUsername = input.getUsername() != null ? input.getUsername().trim().toLowerCase() : "";

        if (userRepository.existsByEmail(cleanEmail)) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(cleanUsername)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(cleanUsername);
        user.setEmail(cleanEmail);

        if (input.getFullName() != null) {
            user.setFullName(input.getFullName().trim());
        }
        if (input.getPhone() != null) {
            user.setPhone(input.getPhone().trim());
        }

        user.setPasswordHash(passwordEncoder.encode(input.getPassword()));
        user.setRole(User.Role.USER);
        user.setStatus(true);

        userRepository.save(user);
        return true;
    }

    public LoginRes authenticate(LoginUser input) {
        String identifier = input.getEmail() != null ? input.getEmail().trim() : "";

        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Email or password is not correct"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        input.getPassword()
                )
        );

        // 3. Tạo JWT Token bảo mật
        String jwtToken = jwtService.generateToken(user);

        // 4. Đóng gói kết quả trả về cho React
        return new LoginRes(
                jwtToken,
                user.getUserId(),
                user.getRealUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name()
        );
    }
}