package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerScheduleWeekReq;
import group3.paws_hope.dto.res.VolunteerScheduleWeekRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerScheduleWeek;
import group3.paws_hope.entity.VolunteerScheduleWindow;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerScheduleRepository;
import group3.paws_hope.repository.VolunteerScheduleWeekRepository;
import group3.paws_hope.repository.VolunteerScheduleWindowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerScheduleWeekService {

    private final VolunteerScheduleWeekRepository volunteerScheduleWeekRepository;
    private final VolunteerScheduleWindowRepository volunteerScheduleWindowRepository;
    private final UserRepository userRepository;
    private final VolunteerScheduleRepository volunteerScheduleRepository;

    public List<VolunteerScheduleWeekRes> getAll() {
        return volunteerScheduleWeekRepository.findAll().stream()
                .map(VolunteerScheduleWeekRes::toJson)
                .toList();
    }

    public VolunteerScheduleWeekRes findById(Long id) {
        VolunteerScheduleWeek week = volunteerScheduleWeekRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule week not found"));

        return VolunteerScheduleWeekRes.toJson(week);
    }

    public VolunteerScheduleWeekRes create(VolunteerScheduleWeekReq req) {
        try {
            VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(req.getWindowId())
                    .orElseThrow(() -> new RuntimeException("Schedule window not found"));

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (volunteerScheduleWeekRepository.existsByUser_UserIdAndWeekStartDate(req.getUserId(),
                    window.getWeekStartDate())) {
                throw new RuntimeException("User already registered this week");
            }

            VolunteerScheduleWeek week = new VolunteerScheduleWeek();

            week.setWindow(window);
            week.setUser(user);
            week.setWeekStartDate(window.getWeekStartDate());
            week.setWeekEndDate(window.getWeekEndDate());
            week.setStatus(VolunteerScheduleWeek.Status.DRAFT);

            return VolunteerScheduleWeekRes.toJson(volunteerScheduleWeekRepository.save(week));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerScheduleWeekRes submit(Long id) {
        try {
            VolunteerScheduleWeek week = volunteerScheduleWeekRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule week not found"));

            int totalDays = volunteerScheduleRepository.countDistinctWorkDateByWeek_WeekIdAndUser_UserId(
                    week.getWeekId(),
                    week.getUser().getUserId()
            );

            if (totalDays < 5) {
                throw new RuntimeException("Volunteer must register at least 5 working days per week");
            }

            week.setStatus(VolunteerScheduleWeek.Status.SUBMITTED);
            week.setSubmittedAt(LocalDateTime.now());

            return VolunteerScheduleWeekRes.toJson(volunteerScheduleWeekRepository.save(week));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerScheduleWeekRes approve(Long id, Long approvedBy) {
        try {
            VolunteerScheduleWeek week = volunteerScheduleWeekRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule week not found"));

            User admin = userRepository.findById(approvedBy)
                    .orElseThrow(() -> new RuntimeException("Approver not found"));

            week.setStatus(VolunteerScheduleWeek.Status.APPROVED);
            week.setApprovedBy(admin);
            week.setApprovedAt(LocalDateTime.now());
            week.setRejectionReason(null);

            return VolunteerScheduleWeekRes.toJson(volunteerScheduleWeekRepository.save(week));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerScheduleWeekRes reject(Long id, Long approvedBy, String reason) {
        try {
            VolunteerScheduleWeek week = volunteerScheduleWeekRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule week not found"));

            User admin = userRepository.findById(approvedBy)
                    .orElseThrow(() -> new RuntimeException("Approver not found"));

            week.setStatus(VolunteerScheduleWeek.Status.REJECTED);
            week.setApprovedBy(admin);
            week.setApprovedAt(LocalDateTime.now());
            week.setRejectionReason(reason);

            return VolunteerScheduleWeekRes.toJson(volunteerScheduleWeekRepository.save(week));

        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        volunteerScheduleWeekRepository.deleteById(id);
    }
}