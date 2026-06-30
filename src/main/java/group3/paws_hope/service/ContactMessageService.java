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

    public List<ContactMessageRes> getAllMessages() {
        return contactMessageRepository.findAll()
                .stream()
                .map(ContactMessageRes::fromEntity)
                .toList();
    }

    public ContactMessageRes getMessageById(Long id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        return ContactMessageRes.fromEntity(message);
    }

    public boolean createMessage(ContactMessageReq req) {
        try {
            ContactMessage msg = new ContactMessage();
            msg.setName(req.getName());
            msg.setEmail(req.getEmail());

            try {
                msg.setSubject(ContactMessage.SubjectType.valueOf(req.getSubject()));
            } catch (Exception e) {
                msg.setSubject(ContactMessage.SubjectType.OTHER);
            }

            msg.setMessage(req.getMessage());
            msg.setStatus(ContactMessage.MessageStatus.UNREAD);

            contactMessageRepository.save(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ContactMessageRes updateStatus(Long id, String status) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setStatus(ContactMessage.MessageStatus.valueOf(status));

        return ContactMessageRes.fromEntity(contactMessageRepository.save(message));
    }
}