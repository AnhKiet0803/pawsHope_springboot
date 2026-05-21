package group3.paws_hope.dto.res;

import group3.paws_hope.entity.EmailLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class EmailLogRes {

    private Long emailId;
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String body;
    private String emailType;
    private String relatedTable;
    private Long relatedId;
    private String status;
    private String errorMessage;
    private Long sentBy;
    private LocalDateTime sentAt;

    public static EmailLogRes toJson(EmailLog log) {
        return new EmailLogRes(
                log.getEmailId(),
                log.getRecipientEmail(),
                log.getRecipientName(),
                log.getSubject(),
                log.getBody(),
                log.getEmailType().name(),
                log.getRelatedTable(),
                log.getRelatedId(),
                log.getStatus().name(),
                log.getErrorMessage(),
                log.getSentBy() != null ? log.getSentBy().getUserId() : null,
                log.getSentAt()
        );
    }
}