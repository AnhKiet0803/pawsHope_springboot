package group3.paws_hope.service;

import group3.paws_hope.dto.req.ContactMessageReq;
import group3.paws_hope.dto.res.ContactMessageRes;
import group3.paws_hope.entity.ContactMessage;
import group3.paws_hope.repository.ContactMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;

    public List<ContactMessageRes> getAll() {
        return contactMessageRepository.findAll().stream()
                .map(ContactMessageRes::toJson)
                .toList();
    }

    public ContactMessageRes create(ContactMessageReq req) {
        ContactMessage entity = new ContactMessage();
        entity.setName(req.getName().trim());
        entity.setEmail(req.getEmail().trim());
        entity.setSubject(parseSubject(req.getSubject()));
        entity.setMessage(req.getMessage().trim());
        entity.setStatus(ContactMessage.Status.UNREAD);
        return ContactMessageRes.toJson(contactMessageRepository.save(entity));
    }

    public ContactMessageRes updateStatus(Long id, String status) {
        ContactMessage entity = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found"));
        entity.setStatus(ContactMessage.Status.valueOf(status));
        return ContactMessageRes.toJson(contactMessageRepository.save(entity));
    }

    private ContactMessage.Subject parseSubject(String raw) {
        if (raw == null || raw.isBlank()) {
            return ContactMessage.Subject.OTHER;
        }
        try {
            return ContactMessage.Subject.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ContactMessage.Subject.OTHER;
        }
    }
}
