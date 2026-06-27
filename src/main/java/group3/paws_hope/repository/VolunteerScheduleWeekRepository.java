package group3.paws_hope.repository;

import group3.paws_hope.entity.VolunteerScheduleWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VolunteerScheduleWeekRepository extends JpaRepository<VolunteerScheduleWeek, Long> {
    boolean existsByUser_UserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);
    List<VolunteerScheduleWeek> findByUser_UserId(Long userId);
    Optional<VolunteerScheduleWeek> findByUser_UserIdAndWindow_WindowId(Long userId, Long windowId);
}