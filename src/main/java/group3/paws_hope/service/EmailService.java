package group3.paws_hope.service;

import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.EmailLogRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final UserRepository userRepository;

    public void sendEmail(
            String to,
            String recipientName,
            String subject,
            String body,
            String relatedTable,
            Long relatedId,
            EmailLog.EmailType emailType,
            Long sentById
    ) {
        EmailLog log = new EmailLog();

        log.setRecipientEmail(to);
        log.setRecipientName(recipientName);
        log.setSubject(subject);
        log.setBody(body);
        log.setEmailType(emailType);
        log.setRelatedTable(relatedTable);
        log.setRelatedId(relatedId);

        if (sentById != null) {
            User sender = userRepository.findById(sentById).orElse(null);
            log.setSentBy(sender);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.setStatus(EmailLog.Status.SENT);
            log.setSentAt(LocalDateTime.now());

        } catch (Exception e) {
            log.setStatus(EmailLog.Status.FAILED);
            log.setErrorMessage(e.getMessage());
        }

        emailLogRepository.save(log);
    }
}