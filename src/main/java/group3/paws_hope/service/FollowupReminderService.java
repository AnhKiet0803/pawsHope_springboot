package group3.paws_hope.service;

import group3.paws_hope.entity.AdoptionFollowup;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.Notification;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.AdoptionFollowupRepository;
import group3.paws_hope.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FollowupReminderService {

    private final AdoptionFollowupRepository adoptionFollowupRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendFollowupReminderBefore3Days() {
        LocalDate targetDate = LocalDate.now().plusDays(3);

        List<AdoptionFollowup> followups =
                adoptionFollowupRepository.findByFollowupDateAndStatus(
                        targetDate,
                        AdoptionFollowup.Status.SCHEDULED
                );

        for (AdoptionFollowup followup : followups) {
            User adopter = followup.getAdoption().getUser();

            Notification noti = new Notification();

            noti.setUser(adopter);
            noti.setMessage("You have a post-adoption follow-up appointment on " + followup.getFollowupDate());
            noti.setType(Notification.Type.SYSTEM);
            noti.setRelatedId(followup.getFollowupId());
            noti.setIsRead(false);

            notificationRepository.save(noti);

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    "Post-adoption follow-up reminder",
                    "Hello " + adopter.getFullName()
                            + ",\n\nYou have a post-adoption follow-up appointment on "
                            + followup.getFollowupDate()
                            + ".\n\nPlease check the Paws Hope system for details.",
                    "adoption_followups",
                    followup.getFollowupId(),
                    EmailLog.EmailType.ADOPTION_FOLLOWUP,
                    null
            );
        }
    }
}