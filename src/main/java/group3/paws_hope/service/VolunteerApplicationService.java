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

            VolunteerApplication savedApp = volunteerApplicationRepository.save(volunteerApplication);

            emailService.sendEmail(
                    savedApp.getEmail(),
                    savedApp.getFullName(),
                    "[PawsHope] Đăng ký Tình nguyện viên thành công",
                    "Xin chào " + savedApp.getFullName() + ",\n\n"
                            + "Cảm ơn bạn đã nộp đơn đăng ký làm tình nguyện viên tại PawsHope.\n"
                            + "Hệ thống đã ghi nhận hồ sơ của bạn ở trạng thái CHỜ DUYỆT. Chúng tôi sẽ đánh giá và sớm gửi thông tin lịch hẹn gặp mặt tới bạn.\n\n"
                            + "Trân trọng,\nPawsHope Team.",
                    "volunteer_applications",
                    savedApp.getApplicationId(),
                    EmailLog.EmailType.VOLUNTEER_INTERVIEW,
                    null
            );

            return VolunteerApplicationRes.toJson(savedApp);

        } catch (Exception e) {
            e.printStackTrace();
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
                        "[PawsHope] Kết quả gặp mặt Tình nguyện viên - Đạt",
                        "Xin chào " + saved.getFullName() + ",\n\n"
                                + "Chúc mừng bạn! Đơn đăng ký tình nguyện viên của bạn đã chính thức được thông qua.\n"
                                + "Chào mừng bạn đã trở thành một phần của đại gia đình PawsHope."
                                + "Một lần nữa, chúng tôi xin cảm ơn bạn tham gia cùng chúng tôi.",
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
                        "[PawsHope] Kết quả gặp mặt Tình nguyện viên - Thông báo",
                        "Xin chào " + saved.getFullName() + ",\n\n"
                                + "Cảm ơn bạn đã dành thời gian nộp đơn và quan tâm đến các hoạt động của PawsHope.\n"
                                + "Dựa trên số lượng hồ sơ hiện tại, rất tiếc chúng tôi chưa thể đồng hành cùng bạn trong đợt tuyển này.\n"
                                + "Lý do cụ thể: " + (rejectionReason != null ? rejectionReason : "Chưa phù hợp tiêu chí đợt này.") + "\n\n"
                                + "Thông tin hồ sơ của bạn vẫn sẽ được lưu trữ để ưu tiên liên hệ cho các chiến dịch thiện nguyện tiếp theo.\n"
                                + "Chúc bạn luôn có thật nhiều sức khỏe!",
                        "volunteer_applications",
                        saved.getApplicationId(),
                        EmailLog.EmailType.VOLUNTEER_RESULT,
                        reviewerId
                );
            }

            return VolunteerApplicationRes.toJson(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Long id) {
        volunteerApplicationRepository.deleteById(id);
    }
}