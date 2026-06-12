package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerScheduleWindowReq;
import group3.paws_hope.dto.res.VolunteerScheduleWindowRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerScheduleWindow;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerScheduleWindowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerScheduleWindowService {

    private final VolunteerScheduleWindowRepository volunteerScheduleWindowRepository;
    private final UserRepository userRepository;

    public List<VolunteerScheduleWindowRes> getAll() {
        return volunteerScheduleWindowRepository.findAll().stream()
                .map(this::syncStatus)
                .map(VolunteerScheduleWindowRes::toJson)
                .toList();
    }

    public VolunteerScheduleWindowRes findById(Long id) {
        VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule window not found"));

        return VolunteerScheduleWindowRes.toJson(syncStatus(window));
    }

    public VolunteerScheduleWindowRes create(VolunteerScheduleWindowReq req) {
        if (volunteerScheduleWindowRepository.existsByWeekStartDate(req.getWeekStartDate())) {
            throw new RuntimeException("This schedule week already exists");
        }

        LocalDate weekStart = req.getWeekStartDate();
        LocalDate weekEnd = weekStart.plusDays(6);

        LocalDate openDate = weekStart.minusDays(4);
        LocalDate closeDate = weekStart.minusDays(1);

        VolunteerScheduleWindow window = new VolunteerScheduleWindow();
        window.setWeekStartDate(weekStart);
        window.setWeekEndDate(weekEnd);
        window.setOpenAt(openDate.atStartOfDay());
        window.setCloseAt(closeDate.atTime(23, 59,59));

        window.setStatus(calculateStatus(window));

        if (req.getCreatedBy() != null) {
            User creator = userRepository.findById(req.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("Creator not found"));
            window.setCreatedBy(creator);
        }

        return VolunteerScheduleWindowRes.toJson(
                volunteerScheduleWindowRepository.save(window)
        );
    }

    public VolunteerScheduleWindowRes update(Long id, VolunteerScheduleWindowReq req) {
        VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule window not found"));

        LocalDate weekStart = req.getWeekStartDate();
        LocalDate weekEnd = weekStart.plusDays(6);

        LocalDate openDate = weekStart.minusDays(4);
        LocalDate closeDate = weekStart.minusDays(1);

        window.setWeekStartDate(weekStart);
        window.setWeekEndDate(weekEnd);
        window.setOpenAt(openDate.atTime(8, 0));
        window.setCloseAt(closeDate.atTime(23, 59));
        window.setStatus(calculateStatus(window));

        return VolunteerScheduleWindowRes.toJson(
                volunteerScheduleWindowRepository.save(window)
        );
    }

    public VolunteerScheduleWindowRes updateStatus(Long id, String status) {
        VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule window not found"));

        window.setStatus(VolunteerScheduleWindow.Status.valueOf(status));

        return VolunteerScheduleWindowRes.toJson(
                volunteerScheduleWindowRepository.save(window)
        );
    }

    public void delete(Long id) {
        volunteerScheduleWindowRepository.deleteById(id);
    }

    private VolunteerScheduleWindow syncStatus(VolunteerScheduleWindow window) {
        VolunteerScheduleWindow.Status newStatus = calculateStatus(window);

        if (window.getStatus() != newStatus) {
            window.setStatus(newStatus);
            return volunteerScheduleWindowRepository.save(window);
        }

        return window;
    }

    private VolunteerScheduleWindow.Status calculateStatus(VolunteerScheduleWindow window) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(window.getOpenAt())) {
            return VolunteerScheduleWindow.Status.NOT_OPEN;
        }

        if (now.isAfter(window.getCloseAt())) {
            return VolunteerScheduleWindow.Status.CLOSED;
        }

        return VolunteerScheduleWindow.Status.OPEN;
    }
}