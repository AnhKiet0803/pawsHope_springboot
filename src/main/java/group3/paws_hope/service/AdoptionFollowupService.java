package group3.paws_hope.service;

import group3.paws_hope.dto.req.AdoptionFollowupReq;
import group3.paws_hope.dto.res.AdoptionFollowupRes;
import group3.paws_hope.entity.Adoption;
import group3.paws_hope.entity.AdoptionFollowup;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.AdoptionFollowupRepository;
import group3.paws_hope.repository.AdoptionRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdoptionFollowupService {

    private final AdoptionFollowupRepository adoptionFollowupRepository;
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;

    public List<AdoptionFollowupRes> getAll() {
        return adoptionFollowupRepository.findAll().stream()
                .map(AdoptionFollowupRes::toJson)
                .toList();
    }

    public AdoptionFollowupRes findById(Long id) {
        AdoptionFollowup followup = adoptionFollowupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Follow up not found"));

        return AdoptionFollowupRes.toJson(followup);
    }

    public List<AdoptionFollowupRes> getByAdoptionId(Long adoptionId) {
        return adoptionFollowupRepository.findByAdoption_AdoptionId(adoptionId).stream()
                .map(AdoptionFollowupRes::toJson)
                .toList();
    }

    public AdoptionFollowupRes create(AdoptionFollowupReq req) {
        try {
            Adoption adoption = adoptionRepository.findById(req.getAdoptionId())
                    .orElseThrow(() -> new RuntimeException("Adoption not found"));

            User admin = userRepository.findById(req.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (admin.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("Only admin can create follow-up");
            }

            AdoptionFollowup adoptionFollowup= new AdoptionFollowup();

            adoptionFollowup.setAdoption(adoption);

            if (req.getFollowupDate() != null) {
                adoptionFollowup.setFollowupDate(req.getFollowupDate());
            } else {
                adoptionFollowup.setFollowupDate(LocalDate.now().plusMonths(6));
            }

            if (req.getFollowupType() != null) {
                adoptionFollowup.setFollowupType(AdoptionFollowup.FollowupType.valueOf(req.getFollowupType()));
            }

            if (req.getPetCondition() != null) {
                adoptionFollowup.setPetCondition(AdoptionFollowup.PetCondition.valueOf(req.getPetCondition()));
            }

            adoptionFollowup.setStatus(AdoptionFollowup.Status.SCHEDULED);
            adoptionFollowup.setAdopterFeedback(req.getAdopterFeedback());
            adoptionFollowup.setStaffNote(req.getStaffNote());
            adoptionFollowup.setPhotoUrl(req.getPhotoUrl());
            adoptionFollowup.setNextFollowupDate(adoptionFollowup.getFollowupDate().plusMonths(6));
            adoptionFollowup.setCreatedBy(admin);

            return AdoptionFollowupRes.toJson(adoptionFollowupRepository.save(adoptionFollowup));
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionFollowupRes confirm(Long id) {
        try {
            AdoptionFollowup followup = adoptionFollowupRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Follow-up not found"));

            followup.setStatus(AdoptionFollowup.Status.CONFIRMED);
            followup.setConfirmedAt(LocalDateTime.now());

            return AdoptionFollowupRes.toJson(adoptionFollowupRepository.save(followup));
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionFollowupRes complete(Long id, AdoptionFollowupReq req) {
        try {
            AdoptionFollowup adoptionFollowup = adoptionFollowupRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Follow-up not found"));

            adoptionFollowup.setStatus(AdoptionFollowup.Status.COMPLETED);
            adoptionFollowup.setCompletedAt(LocalDateTime.now());

            if (req.getPetCondition() != null) {
                adoptionFollowup.setPetCondition(AdoptionFollowup.PetCondition.valueOf(req.getPetCondition()));
            }

            adoptionFollowup.setAdopterFeedback(req.getAdopterFeedback());
            adoptionFollowup.setStaffNote(req.getStaffNote());
            adoptionFollowup.setPhotoUrl(req.getPhotoUrl());
            adoptionFollowup.setNextFollowupDate(adoptionFollowup.getFollowupDate().plusMonths(6));

            return AdoptionFollowupRes.toJson(adoptionFollowupRepository.save(adoptionFollowup));
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionFollowupRes cancel(Long id) {
        try {
            AdoptionFollowup followup = adoptionFollowupRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Follow-up not found"));

            followup.setStatus(AdoptionFollowup.Status.CANCELLED);

            return AdoptionFollowupRes.toJson(adoptionFollowupRepository.save(followup));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        adoptionFollowupRepository.deleteById(id);
    }
}