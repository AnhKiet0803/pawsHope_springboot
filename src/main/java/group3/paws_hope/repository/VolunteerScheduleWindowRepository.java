package group3.paws_hope.repository;

import group3.paws_hope.entity.VolunteerScheduleWindow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface VolunteerScheduleWindowRepository extends JpaRepository<VolunteerScheduleWindow, Long> {
    Optional<VolunteerScheduleWindow> findByWeekStartDate(LocalDate weekStartDate);
    boolean existsByWeekStartDate(LocalDate weekStartDate);
}