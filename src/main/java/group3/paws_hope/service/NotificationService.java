package group3.paws_hope.service;

import group3.paws_hope.dto.req.NotificationReq;
import group3.paws_hope.dto.res.NotificationRes;
import group3.paws_hope.entity.Notification;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.NotificationRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationRes> getAll() {
        return notificationRepository.findAll().stream()
                .map(NotificationRes::toJson)
                .toList();
    }

    public List<NotificationRes> getByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationRes::toJson)
                .toList();
    }

    public List<NotificationRes> getUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(NotificationRes::toJson)
                .toList();
    }

    public Long countUnread(Long userId) {
        return notificationRepository.countByUser_UserIdAndIsReadFalse(userId);
    }

    public NotificationRes create(NotificationReq req) {
        try {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Notification noti = new Notification();
            noti.setUser(user);
            noti.setMessage(req.getMessage());
            noti.setRelatedId(req.getRelatedId());
            noti.setIsRead(false);
            if (req.getType() != null) {
                noti.setType(Notification.Type.valueOf(req.getType()));
            }
            return NotificationRes.toJson(notificationRepository.save(noti));
        } catch (Exception e) {
            return null;
        }
    }

    public void createNotification(User user, String message, Notification.Type type, Long relatedId) {
        Notification noti = new Notification();
        noti.setUser(user);
        noti.setMessage(message);
        noti.setType(type);
        noti.setRelatedId(relatedId);
        noti.setIsRead(false);

        notificationRepository.save(noti);
    }

    public NotificationRes markAsRead(Long id) {
        try {
            Notification noti = notificationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Notification not found"));

            noti.setIsRead(true);

            return NotificationRes.toJson(notificationRepository.save(noti));
        } catch (Exception e) {
            return null;
        }
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications =
                notificationRepository.findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        for (Notification noti : notifications) {
            noti.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}