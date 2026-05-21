package group3.paws_hope.repository;

import group3.paws_hope.entity.VolunteerScheduleWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface VolunteerScheduleWeekRepository extends JpaRepository<VolunteerScheduleWeek, Long> {
    boolean existsByUser_UserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);
}