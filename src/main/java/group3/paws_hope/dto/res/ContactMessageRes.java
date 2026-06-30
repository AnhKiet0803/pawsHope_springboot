package group3.paws_hope.dto.res;

import group3.paws_hope.entity.ContactMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContactMessageRes {
    private Long messageId;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public static ContactMessageRes fromEntity(ContactMessage entity) {
        ContactMessageRes res = new ContactMessageRes();

        res.setMessageId(entity.getMessageId());
        res.setName(entity.getName());
        res.setEmail(entity.getEmail());
        res.setSubject(entity.getSubject().name());
        res.setMessage(entity.getMessage());
        res.setStatus(entity.getStatus().name());
        res.setCreatedAt(entity.getCreatedAt());

        return res;
    }
}