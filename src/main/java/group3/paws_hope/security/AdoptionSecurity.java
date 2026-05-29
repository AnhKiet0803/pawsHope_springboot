package group3.paws_hope.security;

import group3.paws_hope.repository.AdoptionFollowupRepository;
import group3.paws_hope.repository.AdoptionHandoverRepository;
import group3.paws_hope.repository.AdoptionMeetingRepository;
import group3.paws_hope.repository.AdoptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component("adoptionSecurity")
@AllArgsConstructor
public class AdoptionSecurity {
    private final AdoptionRepository adoptionRepository;
    private final AdoptionMeetingRepository meetingRepository;
    private final AdoptionHandoverRepository handoverRepository;
    private final AdoptionFollowupRepository followupRepository;

    public boolean isOwnerByAdoptionId(Long adoptionId, String usernameOrEmail) {
        return adoptionRepository.findById(adoptionId)
                .map(adoption -> {
                    var user = adoption.getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }

    public boolean isOwnerByMeetingId(Long meetingId, String usernameOrEmail) {
        return meetingRepository.findById(meetingId)
                .map(meeting -> {
                    var user = meeting.getAdoption().getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }

    public boolean isOwnerByHandoverId(Long handoverId, String usernameOrEmail) {
        return handoverRepository.findById(handoverId)
                .map(handover -> {
                    var user = handover.getAdoption().getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }

    public boolean isOwnerByFollowupId(Long followupId, String usernameOrEmail) {
        return followupRepository.findById(followupId)
                .map(followup -> {
                    var user = followup.getAdoption().getUser();

                    return user.getEmail().equals(usernameOrEmail)
                            || user.getUsername().equals(usernameOrEmail);
                })
                .orElse(false);
    }
}
