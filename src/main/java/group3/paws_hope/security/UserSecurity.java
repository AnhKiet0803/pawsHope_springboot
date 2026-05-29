package group3.paws_hope.security;

import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@AllArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean isOwner(Long id, String email) {
        return userRepository.findById(id)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }
}
