package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerInterviewReq;
import group3.paws_hope.dto.res.VolunteerInterviewRes;
import group3.paws_hope.entity.EmailLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerApplication;
import group3.paws_hope.entity.VolunteerInterview;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerApplicationRepository;
import group3.paws_hope.repository.VolunteerInterviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerInterviewService {

    private final VolunteerInterviewRepository volunteerInterviewRepository;
    private final VolunteerApplicationRepository volunteerApplicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    public List<VolunteerInterviewRes> getAll() {
        return volunteerInterviewRepository.findAll().stream()
                .map(VolunteerInterviewRes::toJson)
                .toList();
    }

    public VolunteerInterviewRes findById(Long id) {
        VolunteerInterview interview = volunteerInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        return VolunteerInterviewRes.toJson(interview);
    }

    public VolunteerInterviewRes create(VolunteerInterviewReq req) {
        try {
            VolunteerApplication application = volunteerApplicationRepository.findById(req.getApplicationId())
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            User interviewer = userRepository.findById(req.getInterviewerId())
                    .orElseThrow(() -> new RuntimeException("Interviewer not found"));

            VolunteerInterview interview = new VolunteerInterview();

            interview.setApplication(application);
            interview.setInterviewer(interviewer);
            interview.setInterviewDatetime(req.getInterviewDatetime());

            if (req.getMeetingType() != null) {
                interview.setMeetingType(VolunteerInterview.MeetingType.valueOf(req.getMeetingType()));
            }

            interview.setMeetingLink(req.getMeetingLink());
            interview.setLocationText(req.getLocationText());

            if (req.getStatus() != null) {
                interview.setStatus(VolunteerInterview.Status.valueOf(req.getStatus()));
            }

            if (req.getResult() != null) {
                interview.setResult(VolunteerInterview.Result.valueOf(req.getResult()));
            }

            interview.setEvaluationNote(req.getEvaluationNote());

            VolunteerInterview saved = volunteerInterviewRepository.save(interview);

            application.setStatus(VolunteerApplication.Status.INTERVIEW_SCHEDULED);
            volunteerApplicationRepository.save(application);

            String interviewLocation = saved.getMeetingType() == VolunteerInterview.MeetingType.ONLINE
                    ? "Link trực tuyến (Google Meet/Zoom): " + saved.getMeetingLink()
                    : "Địa điểm trực tiếp: " + saved.getLocationText();

            String formattedTime = saved.getInterviewDatetime() != null
                    ? saved.getInterviewDatetime().format(formatter)
                    : "Chưa xác định";

            emailService.sendEmail(
                    application.getEmail(),
                    application.getFullName(),
                    "[PawsHope] Lịch hẹn gặp mặt Tình nguyện viên",
                    "Xin chào " + application.getFullName() + ",\n\n"
                            + "Lời đầu tiên, PawsHope xin cảm ơn sự quan tâm của bạn dành cho các hoạt động cứu hộ động vật của chúng tôi.\n"
                            + "Hồ sơ của bạn đã vượt qua vòng loại. Trân trọng mời bạn tham gia buổi gặp mặt để trao đổi thông tin cụ thể:\n\n"
                            + "⏱ Thời gian: " + formattedTime + "\n"
                            + "Hình thức: " + (saved.getMeetingType() == VolunteerInterview.MeetingType.ONLINE ? "ONLINE" : "OFFLINE") + "\n"
                            + "🔗 " + interviewLocation + "số 10, Đan Phượng, Hà Nội"
                            + "Vui lòng chuẩn bị kết nối mạng ổn định (nếu online) hoặc có mặt trước 5-10 phút (nếu offline) để buổi găp mặt diễn ra tốt đẹp.\n\n"
                            + "Trân trọng,\nPawsHope Team.",
                    "volunteer_interviews",
                    saved.getInterviewId(),
                    EmailLog.EmailType.VOLUNTEER_INTERVIEW,
                    interviewer.getUserId()
            );

            return VolunteerInterviewRes.toJson(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public VolunteerInterviewRes update(Long id, VolunteerInterviewReq req) {
        try {
            VolunteerInterview interview = volunteerInterviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interview not found"));

            User interviewer = userRepository.findById(req.getInterviewerId())
                    .orElseThrow(() -> new RuntimeException("Interviewer not found"));

            interview.setInterviewer(interviewer);
            interview.setInterviewDatetime(req.getInterviewDatetime());

            if (req.getMeetingType() != null) {
                interview.setMeetingType(VolunteerInterview.MeetingType.valueOf(req.getMeetingType()));
            }

            interview.setMeetingLink(req.getMeetingLink());
            interview.setLocationText(req.getLocationText());
            interview.setEvaluationNote(req.getEvaluationNote());

            return VolunteerInterviewRes.toJson(volunteerInterviewRepository.save(interview));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerInterviewRes updateStatus(Long id, String status) {
        try {
            VolunteerInterview interview = volunteerInterviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interview not found"));

            interview.setStatus(VolunteerInterview.Status.valueOf(status));

            return VolunteerInterviewRes.toJson(volunteerInterviewRepository.save(interview));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerInterviewRes updateResult(Long id, String result, String evaluationNote) {
        try {
            VolunteerInterview interview = volunteerInterviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interview not found"));

            VolunteerInterview.Result newResult = VolunteerInterview.Result.valueOf(result);

            interview.setResult(newResult);
            interview.setEvaluationNote(evaluationNote);
            interview.setStatus(VolunteerInterview.Status.COMPLETED);

            VolunteerInterview saved = volunteerInterviewRepository.save(interview);
            VolunteerApplication application = saved.getApplication();

            String subject = "[PawsHope] Thông báo kết quả gặp mặt Tình nguyện viên";
            String body;

            if (newResult == VolunteerInterview.Result.PASSED) {
                application.setStatus(VolunteerApplication.Status.APPROVED);

                body = "Xin chào " + application.getFullName() + ",\n\n"
                        + "Chúc mừng bạn! Bạn đã hoàn thành xuất sắc buổi gặp mặt và chính thức vượt qua thử thách để trở thành Tình nguyện viên của PawsHope.\n"
                        + "Sự đồng hành của bạn sẽ đóng góp một phần rất lớn vào hành trình tìm lại mái ấm cho các bé chó mèo đáng thương.\n\n"
                        + "Vui lòng vào trang web của PawsHope đăng kí tài khoản ( nếu bạn chưa đăng kí ) để chúng tôi cung cấp tài khoản tình nguyện cho bạn."
                        + "Chào mừng bạn gia nhập mái nhà chung PawsHope !";

            } else if (newResult == VolunteerInterview.Result.FAILED) {
                application.setStatus(VolunteerApplication.Status.REJECTED);
                application.setRejectionReason(evaluationNote);

                body = "Xin chào " + application.getFullName() + ",\n\n"
                        + "Cảm ơn bạn đã dành thời gian tham gia buổi gặp mặt tình nguyện viên cùng PawsHope.\n"
                        + "Dù nhận thấy được tình yêu thương động vật to lớn của bạn, tuy nhiên dựa trên các tiêu chí vận hành hiện tại, rất tiếc chúng tôi chưa thể đồng hành cùng bạn trong chiến dịch lần này.\n"
                        + "Ghi chú từ hội đồng: " + (evaluationNote != null ? evaluationNote : "Chưa phù hợp tiêu chí đợt này.") + "\n\n"
                        + "Hồ sơ của bạn vẫn sẽ được lưu giữ trên hệ thống để chúng tôi ưu tiên liên hệ cho các hoạt động cộng đồng phù hợp tiếp theo.\n"
                        + "Chúc bạn luôn giữ vững ngọn lửa nhiệt huyết và gặp nhiều may mắn!";
            } else {
                application.setStatus(VolunteerApplication.Status.INTERVIEWED);

                body = "Xin chào " + application.getFullName() + ",\n\n"
                        + "Hệ thống PawsHope vừa cập nhật trạng thái mới cho buổi gặp mặt tình nguyện viên của bạn.\n"
                        + "Kết quả hiện tại: " + newResult.name() + "\n"
                        + "Vui lòng theo dõi các thông báo tiếp theo từ chúng tôi.";
            }

            volunteerApplicationRepository.save(application);

            emailService.sendEmail(
                    application.getEmail(),
                    application.getFullName(),
                    subject,
                    body,
                    "volunteer_interviews",
                    saved.getInterviewId(),
                    EmailLog.EmailType.VOLUNTEER_RESULT,
                    saved.getInterviewer().getUserId()
            );

            return VolunteerInterviewRes.toJson(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Long id) {
        volunteerInterviewRepository.deleteById(id);
    }
}