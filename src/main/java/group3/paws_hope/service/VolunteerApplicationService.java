package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerApplicationReq;
import group3.paws_hope.dto.res.VolunteerApplicationRes;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerApplication;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerApplicationService {

    private final VolunteerApplicationRepository volunteerApplicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<VolunteerApplicationRes> getAll() {
        return volunteerApplicationRepository.findAll().stream()
                .map(VolunteerApplicationRes::toJson)
                .toList();
    }

    public VolunteerApplicationRes findById(Long id) {
        VolunteerApplication app = volunteerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return VolunteerApplicationRes.toJson(app);
    }

    public VolunteerApplicationRes create(VolunteerApplicationReq req) {
        try {
            VolunteerApplication volunteerApplication = new VolunteerApplication();

            if (req.getUserId() != null) {
                User user = userRepository.findById(req.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                volunteerApplication.setUser(user);
            }

            volunteerApplication.setFullName(req.getFull_name());
            volunteerApplication.setEmail(req.getEmail());
            volunteerApplication.setPhone(req.getPhone());
            volunteerApplication.setDateOfBirth(req.getDateOfBirth());
            volunteerApplication.setAddress(req.getAddress());
            volunteerApplication.setOccupation(req.getOccupation());
            volunteerApplication.setSkills(req.getSkills());
            volunteerApplication.setExperienceWithAnimals(req.getExperienceWithAnimals());
            volunteerApplication.setReasonToJoin(req.getReasonToJoin());
            volunteerApplication.setAvailableDays(req.getAvailableDays());
            volunteerApplication.setPreferredTasks(req.getPreferredTasks());
            volunteerApplication.setHasTransport(req.getHasTransport() != null ? req.getHasTransport() : false);
            volunteerApplication.setStatus(VolunteerApplication.Status.PENDING);

            return VolunteerApplicationRes.toJson(
                    volunteerApplicationRepository.save(volunteerApplication)
            );

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerApplicationRes updateStatus(
            Long id,
            String status,
            Long reviewerId,
            String rejectionReason
    ) {
        try {
            VolunteerApplication volunteerApplication = volunteerApplicationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            User reviewer = userRepository.findById(reviewerId)
                    .orElseThrow(() -> new RuntimeException("Reviewer not found"));

            VolunteerApplication.Status newStatus = VolunteerApplication.Status.valueOf(status);

            volunteerApplication.setStatus(newStatus);
            volunteerApplication.setReviewedBy(reviewer);
            volunteerApplication.setReviewedAt(LocalDateTime.now());
            volunteerApplication.setRejectionReason(rejectionReason);

            VolunteerApplication saved = volunteerApplicationRepository.save(volunteerApplication);

            if (newStatus == VolunteerApplication.Status.APPROVED) {
                emailService.sendEmail(
                        saved.getEmail(),
                        saved.getFullName(),
                        "Kết quả đăng ký tình nguyện viên",
                        "Xin chào " + saved.getFullName()
                                + ",\n\nChúc mừng bạn! Đơn đăng ký tình nguyện viên của bạn đã được duyệt."
                                + "\nBạn có thể đăng nhập hệ thống để theo dõi các hoạt động tiếp theo.",
                        "volunteer_applications",
                        saved.getApplicationId(),
                        EmailLog.EmailType.VOLUNTEER_RESULT,
                        reviewerId
                );
            }

            if (newStatus == VolunteerApplication.Status.REJECTED) {
                emailService.sendEmail(
                        saved.getEmail(),
                        saved.getFullName(),
                        "Kết quả đăng ký tình nguyện viên",
                        "Xin chào " + saved.getFullName()
                                + ",\n\nRất tiếc, đơn đăng ký tình nguyện viên của bạn chưa được duyệt."
                                + "\nLý do: " + rejectionReason,
                        "volunteer_applications",
                        saved.getApplicationId(),
                        EmailLog.EmailType.VOLUNTEER_RESULT,
                        reviewerId
                );
            }

            return VolunteerApplicationRes.toJson(saved);

        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        volunteerApplicationRepository.deleteById(id);
    }
}