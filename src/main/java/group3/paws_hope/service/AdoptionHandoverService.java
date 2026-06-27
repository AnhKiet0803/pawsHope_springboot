package group3.paws_hope.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile; // 🌟 Thêm import này

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // 🌟 Thêm import này

@Service
@AllArgsConstructor
public class AdoptionHandoverService {

    private final AdoptionHandoverRepository adoptionHandoverRepository;
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final EmailService emailService;
    private final Cloudinary cloudinary;

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
                String method = req.getHandoverMethod().toUpperCase();

                if (method.contains("PICKUP") || method.contains("AT_SHELTER")) {
                    handover.setHandoverMethod(AdoptionHandover.HandoverMethod.AT_SHELTER);
                } else if (method.contains("HOME")) {
                    handover.setHandoverMethod(AdoptionHandover.HandoverMethod.HOME_VISIT);
                } else if (method.contains("MEETUP")) {
                    handover.setHandoverMethod(AdoptionHandover.HandoverMethod.MEETUP_POINT);
                } else {
                    throw new RuntimeException("Phương thức bàn giao không hợp lệ: " + method);
                }
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
            System.err.println("LỖI TẠO HANDOVER: " + e.getMessage());
            e.printStackTrace();
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

    public AdoptionHandoverRes complete(Long id, String completionNote, MultipartFile file) {
        try {
            AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover not found"));

            String secureUrl = null;
            if (file != null && !file.isEmpty()) {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "pawshope_handovers"
                ));
                secureUrl = (String) uploadResult.get("secure_url");
            }

            handover.setStatus(AdoptionHandover.Status.COMPLETED);
            handover.setCompletedAt(LocalDateTime.now());
            handover.setCompletionNote(completionNote);
            if (secureUrl != null) {
                handover.setHandoverPhotoUrl(secureUrl);
            }

            Adoption adoption = handover.getAdoption();
            adoption.setStatus(Adoption.Status.COMPLETED);
            adoptionRepository.save(adoption);

            Pet pet = adoption.getPet();
            pet.setStatus(Pet.Status.ADOPTED);
            petRepository.save(pet);

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(handover));
        } catch (Exception e) {
            System.err.println("LỖI HOÀN THÀNH BÀN GIAO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public AdoptionHandoverRes updateStatus(Long id, String statusStr) {
        try {
            AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover arrangement not found"));

            AdoptionHandover.Status newStatus = AdoptionHandover.Status.valueOf(statusStr.toUpperCase());
            handover.setStatus(newStatus);

            if (newStatus == AdoptionHandover.Status.CONFIRMED && handover.getCompletionNote() != null) {
                String note = handover.getCompletionNote();

                if (note.contains("Date: ")) {
                    int dateIdx = note.indexOf("Date: ") + 6;
                    String datePart = note.substring(dateIdx, dateIdx + 10);
                    String hourPart = "08:30:00";
                    if (note.contains("Time: Noon")) hourPart = "11:30:00";
                    else if (note.contains("Time: Afternoon")) hourPart = "14:00:00";
                    else if (note.contains("Time: Evening")) hourPart = "17:00:00";

                    String isoDateTime = datePart + "T" + hourPart;
                    handover.setPickupDatetime(java.time.LocalDateTime.parse(isoDateTime));
                }
                handover.setAdopterConfirmed(false);
            }

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(handover));
        } catch (Exception e) {
            System.err.println("LỖI CẬP NHẬT TRẠNG THÁI BÀN GIAO: " + e.getMessage());
            return null;
        }
    }

    public void delete(Long id) {
        adoptionHandoverRepository.deleteById(id);
    }

    public AdoptionHandoverRes requestReschedule(Long id, String note) {
        try {
            AdoptionHandover handover = adoptionHandoverRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Handover arrangement not found"));

            handover.setStatus(AdoptionHandover.Status.RESCHEDULED);
            handover.setCompletionNote(note);

            return AdoptionHandoverRes.toJson(adoptionHandoverRepository.save(handover));
        } catch (Exception e) {
            return null;
        }
    }
}