package group3.paws_hope.security;

import group3.paws_hope.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("notificationSecurity")
@AllArgsConstructor
public class NotificationSecurity {
    private final NotificationRepository notificationRepository;

    public boolean isOwner(Long notificationId, String usernameOrEmail) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    var user = notification.getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }
}