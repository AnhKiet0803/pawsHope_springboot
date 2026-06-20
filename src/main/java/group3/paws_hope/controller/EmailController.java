package group3.paws_hope.controller;

import group3.paws_hope.dto.req.EmailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailReq request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("huyenptanifc@gmail.com"); // Email gửi đi (trùng với username cấu hình)
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getContent());

            mailSender.send(message); // Thực hiện gửi qua Gmail SMTP

            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}