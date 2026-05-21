package group3.paws_hope.service;

import group3.paws_hope.dto.req.AdoptionReq;
import group3.paws_hope.dto.res.AdoptionRes;
import group3.paws_hope.entity.Adoption;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.Pet;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.AdoptionRepository;
import group3.paws_hope.repository.PetRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<AdoptionRes> getAll() {
        return adoptionRepository.findAll().stream()
                .map(AdoptionRes::toJson)
                .toList();
    }

    public AdoptionRes findById(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption application not found"));

        return AdoptionRes.toJson(adoption);
    }

    public AdoptionRes getByApplicationCode(String code) {
        Adoption adoption = adoptionRepository.findByApplicationCode(code)
                .orElseThrow(() -> new RuntimeException("Application code not found"));

        return AdoptionRes.toJson(adoption);
    }

    public List<AdoptionRes> getByUserId(Long userId) {
        return adoptionRepository.findByUser_UserId(userId).stream()
                .map(AdoptionRes::toJson)
                .toList();
    }

    public AdoptionRes create(AdoptionReq req) {
        try {
            Pet pet = petRepository.findById(req.getPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (pet.getStatus() != Pet.Status.AVAILABLE_FOR_ADOPTION) {
                throw new RuntimeException("This pet is not available for adoption");
            }

            Adoption adoption = new Adoption();

            adoption.setApplicationCode(generateApplicationCode());
            adoption.setPet(pet);
            adoption.setUser(user);
            adoption.setApplicantAddress(req.getApplicantAddress());

            if (req.getHousingType() != null) {
                adoption.setHousingType(Adoption.HousingType.valueOf(req.getHousingType()));
            }

            adoption.setHasPetExperience(req.getHasPetExperience() != null ? req.getHasPetExperience() : false);
            adoption.setCurrentPets(req.getCurrentPets());
            adoption.setWorkingSchedule(req.getWorkingSchedule());
            adoption.setReason(req.getReason());
            adoption.setFamilyAgreement(req.getFamilyAgreement() != null ? req.getFamilyAgreement() : true);
            adoption.setFinancialCommitment(req.getFinancialCommitment() != null ? req.getFinancialCommitment() : true);
            adoption.setApplyDate(LocalDate.now());
            adoption.setStatus(Adoption.Status.PENDING);

            if (req.getPriorityLevel() != null) {
                adoption.setPriorityLevel(Adoption.PriorityLevel.valueOf(req.getPriorityLevel()));
            }

            if (req.getReviewStatus() != null) {
                adoption.setReviewStatus(Adoption.ReviewStatus.valueOf(req.getReviewStatus()));
            }

            adoption.setMissingInfoNote(req.getMissingInfoNote());
            adoption.setAdoptionFee(req.getAdoptionFee());
            adoption.setNotes(req.getNotes());

            pet.setStatus(Pet.Status.PENDING_ADOPTION);
            petRepository.save(pet);

            return AdoptionRes.toJson(adoptionRepository.save(adoption));

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionRes approve(Long id, Long processedBy) {
        try {
            Adoption adoption = adoptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Adoption application not found"));

            User staff = userRepository.findById(processedBy)
                    .orElseThrow(() -> new RuntimeException("Staff not found"));

            adoption.setStatus(Adoption.Status.APPROVED);
            adoption.setProcessedBy(staff);
            adoption.setReviewedAt(LocalDateTime.now());

            Adoption saved = adoptionRepository.save(adoption);

            User adopter = saved.getUser();

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    "Kết quả đơn nhận nuôi",
                    "Xin chào " + adopter.getFullName()
                            + ",\n\nĐơn nhận nuôi của bạn đã được duyệt."
                            + "\nVui lòng theo dõi lịch hẹn bàn giao thú cưng trên hệ thống.",
                    "adoptions",
                    saved.getAdoptionId(),
                    EmailLog.EmailType.ADOPTION_RESULT,
                    processedBy
            );

            return AdoptionRes.toJson(saved);

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionRes reject(Long id, Long processedBy, String note) {
        try {
            Adoption adoption = adoptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Adoption application not found"));

            User staff = userRepository.findById(processedBy)
                    .orElseThrow(() -> new RuntimeException("Staff not found"));

            adoption.setStatus(Adoption.Status.REJECTED);
            adoption.setProcessedBy(staff);
            adoption.setReviewedAt(LocalDateTime.now());
            adoption.setNotes(note);

            Pet pet = adoption.getPet();
            pet.setStatus(Pet.Status.AVAILABLE_FOR_ADOPTION);
            petRepository.save(pet);

            Adoption saved = adoptionRepository.save(adoption);

            User adopter = saved.getUser();

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    "Kết quả đơn nhận nuôi",
                    "Xin chào " + adopter.getFullName()
                            + ",\n\nRất tiếc, đơn nhận nuôi của bạn chưa được duyệt."
                            + "\nGhi chú: " + note,
                    "adoptions",
                    saved.getAdoptionId(),
                    EmailLog.EmailType.ADOPTION_RESULT,
                    processedBy
            );

            return AdoptionRes.toJson(saved);

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionRes updatePaymentStatus(Long id, String paymentStatus) {
        try {
            Adoption adoption = adoptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Adoption application not found"));

            adoption.setPaymentStatus(Adoption.PaymentStatus.valueOf(paymentStatus));

            if (Adoption.PaymentStatus.valueOf(paymentStatus) == Adoption.PaymentStatus.PAID) {
                adoption.setPaidAt(LocalDateTime.now());
            }

            return AdoptionRes.toJson(adoptionRepository.save(adoption));

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionRes complete(Long id) {
        try {
            Adoption adoption = adoptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Adoption application not found"));

            adoption.setStatus(Adoption.Status.COMPLETED);

            Pet pet = adoption.getPet();
            pet.setStatus(Pet.Status.ADOPTED);
            petRepository.save(pet);

            return AdoptionRes.toJson(adoptionRepository.save(adoption));

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionRes cancel(Long id) {
        try {
            Adoption adoption = adoptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Adoption application not found"));

            adoption.setStatus(Adoption.Status.CANCELLED);

            Pet pet = adoption.getPet();
            pet.setStatus(Pet.Status.AVAILABLE_FOR_ADOPTION);
            petRepository.save(pet);

            return AdoptionRes.toJson(adoptionRepository.save(adoption));

        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        adoptionRepository.deleteById(id);
    }

    private String generateApplicationCode() {
        return "AD" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}