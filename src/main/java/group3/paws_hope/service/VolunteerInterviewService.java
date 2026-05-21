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

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerInterviewService {

    private final VolunteerInterviewRepository volunteerInterviewRepository;
    private final VolunteerApplicationRepository volunteerApplicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

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
                    ? saved.getMeetingLink()
                    : saved.getLocationText();

            emailService.sendEmail(
                    application.getEmail(),
                    application.getFullName(),
                    "Thông báo lịch phỏng vấn tình nguyện viên",
                    "Xin chào " + application.getFullName()
                            + ",\n\nBạn có lịch phỏng vấn tình nguyện viên vào "
                            + saved.getInterviewDatetime()
                            + ".\nHình thức: " + saved.getMeetingType().name()
                            + "\nThông tin tham gia: " + interviewLocation
                            + "\n\nVui lòng tham gia đúng giờ.",
                    "volunteer_interviews",
                    saved.getInterviewId(),
                    EmailLog.EmailType.VOLUNTEER_INTERVIEW,
                    interviewer.getUserId()
            );

            return VolunteerInterviewRes.toJson(saved);

        } catch (Exception e) {
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

            String subject = "Kết quả phỏng vấn tình nguyện viên";
            String body;

            if (newResult == VolunteerInterview.Result.PASSED) {
                application.setStatus(VolunteerApplication.Status.APPROVED);

                body = "Xin chào " + application.getFullName()
                        + ",\n\nChúc mừng bạn đã vượt qua buổi phỏng vấn tình nguyện viên."
                        + "\nBạn đã được duyệt tham gia đội ngũ tình nguyện viên của Paws Hope.";
            } else if (newResult == VolunteerInterview.Result.FAILED) {
                application.setStatus(VolunteerApplication.Status.REJECTED);
                application.setRejectionReason(evaluationNote);

                body = "Xin chào " + application.getFullName()
                        + ",\n\nRất tiếc, bạn chưa vượt qua buổi phỏng vấn tình nguyện viên."
                        + "\nGhi chú: " + evaluationNote;
            } else {
                application.setStatus(VolunteerApplication.Status.INTERVIEWED);

                body = "Xin chào " + application.getFullName()
                        + ",\n\nBuổi phỏng vấn tình nguyện viên của bạn đã được cập nhật kết quả."
                        + "\nKết quả hiện tại: " + newResult.name();
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
            return null;
        }
    }

    public void delete(Long id) {
        volunteerInterviewRepository.deleteById(id);
    }
}