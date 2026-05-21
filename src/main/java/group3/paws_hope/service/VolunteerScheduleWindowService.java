package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerScheduleWindowReq;
import group3.paws_hope.dto.res.VolunteerScheduleWindowRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerScheduleWindow;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerScheduleWindowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerScheduleWindowService {

    private final VolunteerScheduleWindowRepository volunteerScheduleWindowRepository;
    private final UserRepository userRepository;

    public List<VolunteerScheduleWindowRes> getAll() {
        return volunteerScheduleWindowRepository.findAll().stream()
                .map(VolunteerScheduleWindowRes::toJson)
                .toList();
    }

    public VolunteerScheduleWindowRes findById(Long id) {
        VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule window not found"));

        return VolunteerScheduleWindowRes.toJson(window);
    }

    public VolunteerScheduleWindowRes create(VolunteerScheduleWindowReq req) {
        try {
            if (volunteerScheduleWindowRepository.existsByWeekStartDate(req.getWeekStartDate())) {
                throw new RuntimeException("This schedule week already exists");
            }

            VolunteerScheduleWindow window = new VolunteerScheduleWindow();
            window.setWeekStartDate(req.getWeekStartDate());
            window.setWeekEndDate(req.getWeekEndDate());
            window.setOpenAt(req.getOpenAt());
            window.setCloseAt(req.getCloseAt());

            if (req.getStatus() != null) {
                window.setStatus(VolunteerScheduleWindow.Status.valueOf(req.getStatus()));
            } else {
                window.setStatus(VolunteerScheduleWindow.Status.NOT_OPEN);
            }

            if (req.getCreatedBy() != null) {
                User creator = userRepository.findById(req.getCreatedBy())
                        .orElseThrow(() -> new RuntimeException("Creator not found"));
                window.setCreatedBy(creator);
            }

            return VolunteerScheduleWindowRes.toJson(volunteerScheduleWindowRepository.save(window));

        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerScheduleWindowRes update(Long id, VolunteerScheduleWindowReq req) {
        try {
            VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule window not found"));

            window.setWeekStartDate(req.getWeekStartDate());
            window.setWeekEndDate(req.getWeekEndDate());
            window.setOpenAt(req.getOpenAt());
            window.setCloseAt(req.getCloseAt());

            if (req.getStatus() != null) {
                window.setStatus(VolunteerScheduleWindow.Status.valueOf(req.getStatus()));
            }

            return VolunteerScheduleWindowRes.toJson(volunteerScheduleWindowRepository.save(window));
        } catch (Exception e) {
            return null;
        }
    }

    public VolunteerScheduleWindowRes updateStatus(Long id, String status) {
        try {
            VolunteerScheduleWindow window = volunteerScheduleWindowRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule window not found"));

            window.setStatus(VolunteerScheduleWindow.Status.valueOf(status));

            return VolunteerScheduleWindowRes.toJson(volunteerScheduleWindowRepository.save(window));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        volunteerScheduleWindowRepository.deleteById(id);
    }
}