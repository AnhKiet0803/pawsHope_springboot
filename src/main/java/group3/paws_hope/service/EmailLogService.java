package group3.paws_hope.service;

import group3.paws_hope.dto.res.EmailLogRes;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.EmailLogRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailLogService {

    private final EmailLogRepository emailLogRepository;
    private final UserRepository userRepository;

    public List<EmailLogRes> getAll(Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view email logs");
        }

        return emailLogRepository.findAll().stream()
                .map(EmailLogRes::toJson)
                .toList();
    }

    public EmailLogRes findById(Long id, Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view email logs");
        }

        EmailLog log = emailLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email log not found"));

        return EmailLogRes.toJson(log);
    }
}