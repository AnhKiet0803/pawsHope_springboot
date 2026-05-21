package group3.paws_hope.service;

import group3.paws_hope.dto.req.VolunteerScheduleReq;
import group3.paws_hope.dto.res.VolunteerScheduleRes;
import group3.paws_hope.entity.Shift;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.VolunteerSchedule;
import group3.paws_hope.entity.VolunteerScheduleWeek;
import group3.paws_hope.repository.ShiftRepository;
import group3.paws_hope.repository.UserRepository;
import group3.paws_hope.repository.VolunteerScheduleRepository;
import group3.paws_hope.repository.VolunteerScheduleWeekRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerScheduleService {

    private final VolunteerScheduleRepository volunteerScheduleRepository;
    private final VolunteerScheduleWeekRepository volunteerScheduleWeekRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    public List<VolunteerScheduleRes> getAll() {
        return volunteerScheduleRepository.findAll().stream()
                .map(VolunteerScheduleRes::toJson)
                .toList();
    }

    public VolunteerScheduleRes findById(Long id) {
        VolunteerSchedule schedule = volunteerScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        return VolunteerScheduleRes.toJson(schedule);
    }

    public VolunteerScheduleRes create(VolunteerScheduleReq req) {
        try {
            VolunteerScheduleWeek week = volunteerScheduleWeekRepository.findById(req.getWeekId())
                    .orElseThrow(() -> new RuntimeException("Schedule week not found"));

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Shift shift = shiftRepository.findById(req.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));

            if (!week.getUser().getUserId().equals(req.getUserId())) {
                throw new RuntimeException("User does not own this schedule week");
            }

            if (req.getWorkDate().isBefore(week.getWeekStartDate())
                    || req.getWorkDate().isAfter(week.getWeekEndDate())) {
                throw new RuntimeException("Work date must be inside this schedule week");
            }

            int totalRegistered = volunteerScheduleRepository.countByShift_ShiftIdAndWorkDate(
                    req.getShiftId(),
                    req.getWorkDate()
            );

            if (totalRegistered >= 3) {
                throw new RuntimeException("This shift is full. Maximum 3 volunteers allowed.");
            }

            VolunteerSchedule schedule = new VolunteerSchedule();

            schedule.setWeek(week);
            schedule.setUser(user);
            schedule.setShift(shift);
            schedule.setWorkDate(req.getWorkDate());

            return VolunteerScheduleRes.toJson(volunteerScheduleRepository.save(schedule));

        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        volunteerScheduleRepository.deleteById(id);
    }
}