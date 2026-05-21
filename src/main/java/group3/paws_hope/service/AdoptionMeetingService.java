package group3.paws_hope.service;

import group3.paws_hope.dto.req.AdoptionMeetingReq;
import group3.paws_hope.dto.res.AdoptionMeetingRes;
import group3.paws_hope.entity.Adoption;
import group3.paws_hope.entity.AdoptionMeeting;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.AdoptionMeetingRepository;
import group3.paws_hope.repository.AdoptionRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdoptionMeetingService {
    private final AdoptionMeetingRepository adoptionMeetingRepository;
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<AdoptionMeetingRes> getAll() {
        return adoptionMeetingRepository.findAll().stream()
                .map(AdoptionMeetingRes::toJson)
                .toList();
    }

    public AdoptionMeetingRes findById(Long id) {
        AdoptionMeeting meeting = adoptionMeetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        return AdoptionMeetingRes.toJson(meeting);
    }

    public List<AdoptionMeetingRes> getByAdoptionId(Long adoptionId) {
        return adoptionMeetingRepository.findByAdoption_AdoptionId(adoptionId).stream()
                .map(AdoptionMeetingRes::toJson)
                .toList();
    }

    public AdoptionMeetingRes create(AdoptionMeetingReq req) {
        try {
            Adoption adoption = adoptionRepository.findById(req.getAdoptionId())
                    .orElseThrow(() -> new RuntimeException("Adoption not found"));

            User staff = userRepository.findById(req.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));

            AdoptionMeeting meeting = new AdoptionMeeting();

            meeting.setAdoption(adoption);
            meeting.setStaff(staff);
            meeting.setMeetingDatetime(req.getMeetingDatetime());
            meeting.setMeetingLocation(req.getMeetingLocation());
            if (req.getStatus() != null) {
                meeting.setStatus(AdoptionMeeting.Status.valueOf(req.getStatus()));
            }

            if (req.getResult() != null) {
                meeting.setResult(AdoptionMeeting.Result.valueOf(req.getResult()));
            }
            meeting.setHousingCheckResult(req.getHousingCheckResult());
            meeting.setExperienceEvaluation(req.getExperienceEvaluation());
            meeting.setNote(req.getNote());

            adoption.setStatus(Adoption.Status.MEETING_SCHEDULED);
            adoptionRepository.save(adoption);

            AdoptionMeeting saved = adoptionMeetingRepository.save(meeting);

            User adopter = adoption.getUser();

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    "Thông báo lịch hẹn gặp mặt nhận nuôi",
                    "Xin chào " + adopter.getFullName()
                            + ",\n\nBạn có lịch hẹn gặp mặt nhận nuôi vào "
                            + saved.getMeetingDatetime()
                            + ".\nĐịa điểm: " + saved.getMeetingLocation()
                            + "\n\nVui lòng đến đúng giờ.",
                    "adoption_meetings",
                    saved.getMeetingId(),
                    EmailLog.EmailType.ADOPTION_MEETING,
                    req.getStaffId()
            );

            return AdoptionMeetingRes.toJson(saved);
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionMeetingRes updateResult(Long id, String result, String note) {
        try {
            AdoptionMeeting meeting = adoptionMeetingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

            meeting.setResult(AdoptionMeeting.Result.valueOf(result));
            meeting.setStatus(AdoptionMeeting.Status.COMPLETED);
            meeting.setNote(note);

            Adoption adoption = meeting.getAdoption();

            if (AdoptionMeeting.Result.valueOf(result) == AdoptionMeeting.Result.PASSED) {
                adoption.setStatus(Adoption.Status.APPROVED);
            } else if (AdoptionMeeting.Result.valueOf(result) == AdoptionMeeting.Result.FAILED) {
                adoption.setStatus(Adoption.Status.REJECTED);
            }

            adoptionRepository.save(adoption);

            AdoptionMeeting saved = adoptionMeetingRepository.save(meeting);

            User adopter = adoption.getUser();

            String subject = "Kết quả buổi gặp mặt nhận nuôi";
            String body;

            if (saved.getResult() == AdoptionMeeting.Result.PASSED) {
                body = "Xin chào " + adopter.getFullName()
                        + ",\n\nBuổi gặp mặt nhận nuôi của bạn đã đạt yêu cầu."
                        + "\nĐơn nhận nuôi của bạn đã được chuyển sang trạng thái APPROVED.";
            } else {
                body = "Xin chào " + adopter.getFullName()
                        + ",\n\nRất tiếc, buổi gặp mặt nhận nuôi của bạn chưa đạt yêu cầu."
                        + "\nGhi chú: " + note;
            }

            emailService.sendEmail(
                    adopter.getEmail(),
                    adopter.getFullName(),
                    subject,
                    body,
                    "adoption_meetings",
                    saved.getMeetingId(),
                    EmailLog.EmailType.ADOPTION_RESULT,
                    saved.getStaff().getUserId()
            );

            return AdoptionMeetingRes.toJson(saved);
        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionMeetingRes updateStatus(Long id, String status) {
        try {
            AdoptionMeeting meeting = adoptionMeetingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

            meeting.setStatus(AdoptionMeeting.Status.valueOf(status));

            return AdoptionMeetingRes.toJson(adoptionMeetingRepository.save(meeting));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        adoptionMeetingRepository.deleteById(id);
    }
}