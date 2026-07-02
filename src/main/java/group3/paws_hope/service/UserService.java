package group3.paws_hope.service;

import group3.paws_hope.dto.req.LoginReq;
import group3.paws_hope.dto.req.UserReq;
import group3.paws_hope.dto.res.UserRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<UserRes> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserRes::toJson)
                .toList();
    }

    public UserRes findById(Long id) {
        return UserRes.toJson(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserRes create(UserReq req) {
        try {
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPasswordHash(req.getPasswordHash());
            user.setFullName(req.getFullName());
            user.setEmail(req.getEmail());
            user.setPhone(req.getPhone());
            if (req.getRole() != null) {
                user.setRole(User.Role.valueOf(req.getRole()));
            } else {
                user.setRole(User.Role.USER);
            }
            user.setStatus(true);
            return UserRes.toJson(userRepository.save(user));
        } catch (Exception e) {
            return null;
        }
    }

    public UserRes update(Long id, UserReq req) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setFullName(req.getFullName());
            user.setPhone(req.getPhone());
            user.setEmail(req.getEmail());

            return UserRes.toJson(userRepository.save(user));
        } catch (Exception e) {
            return null;
        }
    }

    public UserRes updateRole(Long id, String role) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(User.Role.valueOf(role));
            return UserRes.toJson(userRepository.save(user));
        } catch (Exception e) {
            return null;
        }
    }

    public UserRes updateStatus(Long id, Boolean status) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(status);
            return UserRes.toJson(userRepository.save(user));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Map<String, Object> login(LoginReq req) {
        String identifier = req.getUsername().trim();

        User user = userRepository.findByEmail(req.getUsername().trim())
                .orElseThrow(() -> new RuntimeException("Account does not exist!"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Incorrect account or password.!");
        }

        if (Boolean.FALSE.equals(user.getStatus())) {
            throw new RuntimeException("Your account has been disabled.");
        }

        String realToken = jwtService.generateToken(user);

        Map<String, Object> res = new HashMap<>();
        res.put("token", realToken);
        res.put("userId", user.getUserId());
        res.put("username", user.getUsername());
        res.put("email", user.getEmail());
        res.put("fullName", user.getFullName() != null ? user.getFullName() : "Admin");
        res.put("phone", user.getPhone());
        res.put("role", user.getRole().name());

        return res;
    }
}