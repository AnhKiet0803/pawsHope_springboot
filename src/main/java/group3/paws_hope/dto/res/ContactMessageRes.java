package group3.paws_hope.dto.res;

import group3.paws_hope.entity.ContactMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class ContactMessageRes {
    private Long messageId;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String status;
    private Timestamp createdAt;

    public static ContactMessageRes toJson(ContactMessage entity) {
        return new ContactMessageRes(
                entity.getMessageId(),
                entity.getName(),
                entity.getEmail(),
                entity.getSubject() != null ? entity.getSubject().name() : null,
                entity.getMessage(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getCreatedAt()
        );
    }
}
