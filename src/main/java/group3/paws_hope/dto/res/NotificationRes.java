package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRes {

    private Long notiId;
    private Long userId;
    private String message;
    private String type;
    private Long relatedId;
    private Boolean isRead;
    private Timestamp createdAt;

    public static NotificationRes toJson(Notification noti) {
        return new NotificationRes(
                noti.getNotiId(),
                noti.getUser().getUserId(),
                noti.getMessage(),
                noti.getType().name(),
                noti.getRelatedId(),
                noti.getIsRead(),
                noti.getCreatedAt()
        );
    }
}