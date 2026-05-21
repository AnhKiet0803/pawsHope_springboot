package group3.paws_hope.service;

import group3.paws_hope.dto.req.UserReq;
import group3.paws_hope.dto.res.UserRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}