package group3.paws_hope.service;

import group3.paws_hope.dto.req.AdoptionHandoverReq;
import group3.paws_hope.dto.res.AdoptionHandoverRes;
import group3.paws_hope.entity.Adoption;
import group3.paws_hope.entity.AdoptionHandover;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.Pet;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.AdoptionHandoverRepository;
import group3.paws_hope.repository.AdoptionRepository;
import group3.paws_hope.repository.PetRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdoptionHandoverService {

    private final AdoptionHandoverRepository adoptionHandoverRepository;
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final EmailService emailService;

    public List<AdoptionHandoverRes> getAll() {
        return adoptionHandoverRepository.findAll().stream()
                .map(AdoptionHandoverRes::toJson)
                .toList();
    }

    public AdoptionHandoverRes findById(Long id) {
        AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Handover not found"));

        return AdoptionHandoverRes.toJson(handover);
    }

    public List<AdoptionHandoverRes> getByAdoptionId(Long adoptionId) {
        return adoptionHandoverRepository.findByAdoption_AdoptionId(adoptionId).stream()
                .map(AdoptionHandoverRes::toJson)
                .toList();
    }

    public AdoptionHandoverRes create(AdoptionHandoverReq req) {
        try {
            Adoption adoption = adoptionRepository.findById(req.getAdoptionId())
                    .orElseThrow(() -> new RuntimeException("Adoption not found"));

            AdoptionHandover handover = new AdoptionHandover();

            handover.setAdoption(adoption);
            handover.setPickupDatetime(req.getPickupDatetime());
            handover.setPickupLocation(req.getPickupLocation());

            if (req.getHandledBy() != null) {
                User staff = userRepository.findById(req.getHandledBy())
                        .orElseThrow(() -> new RuntimeException("Staff not found"));
                handover.setHandledBy(staff);
            }

            if (req.getHandoverMethod() != null) {
                handover.setHandoverMethod(AdoptionHandover.HandoverMethod.valueOf(req.getHandoverMethod()));
            }

            if (req.getStatus() != null) {
                handover.setStatus(AdoptionHandover.Status.valueOf(req.getStatus()));
            }

            handover.setAdopterConfirmed(req.getAdopterConfirmed() != null ? req.getAdopterConfirmed() : false);
            handover.setItemsGiven(req.getItemsGiven());
            handover.setHandoverPhotoUrl(req.getHandoverPhotoUrl());
            handover.setCompletionNote(req.getCompletionNote());

            adoption.setStatus(Adoption.Status.HANDOVER_SCHEDULED);
            adoptionRepository.save(adoption);

            AdoptionHandover saved = adoptionHandoverRepository.save(handover);

            User adopter = adoption.getUser();

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    "Thông báo lịch hẹn nhận thú cưng",
                    "Xin chào " + adopter.getFullName()
                            + ",\n\nBạn có lịch nhận thú cưng vào "
                            + saved.getPickupDatetime()
                            + ".\nĐịa điểm: " + saved.getPickupLocation()
                            + "\nHình thức bàn giao: " + saved.getHandoverMethod().name()
                            + "\n\nVui lòng đến đúng giờ.",
                    "adoption_handovers",
                    saved.getHandoverId(),
                    EmailLog.EmailType.ADOPTION_HANDOVER,
                    req.getHandledBy()
            );

            return AdoptionHandoverRes.toJson(saved);

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionHandoverRes confirm(Long id) {
        try {
            AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover not found"));

            handover.setStatus(AdoptionHandover.Status.CONFIRMED);
            handover.setAdopterConfirmed(true);

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(handover));
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionHandoverRes complete(Long id, String completionNote) {
        try {
            AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover not found"));

            handover.setStatus(AdoptionHandover.Status.COMPLETED);
            handover.setCompletedAt(LocalDateTime.now());
            handover.setCompletionNote(completionNote);

            Adoption adoption = handover.getAdoption();
            adoption.setStatus(Adoption.Status.COMPLETED);
            adoptionRepository.save(adoption);

            Pet pet = adoption.getPet();
            pet.setStatus(Pet.Status.ADOPTED);
            petRepository.save(pet);

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(handover));
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionHandoverRes updateStatus(Long id, String status) {
        try {
            AdoptionHandover adoptionHandover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover not found"));

            adoptionHandover.setStatus(AdoptionHandover.Status.valueOf(status));

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(adoptionHandover));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        adoptionHandoverRepository.deleteById(id);
    }
}