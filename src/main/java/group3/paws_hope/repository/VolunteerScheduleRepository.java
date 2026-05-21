package group3.paws_hope.repository;

import group3.paws_hope.entity.VolunteerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VolunteerScheduleRepository extends JpaRepository<VolunteerSchedule,Long> {
    int countByShift_ShiftIdAndWorkDate(Long shiftId, LocalDate workDate);
    int countDistinctWorkDateByWeek_WeekIdAndUser_UserId(Long weekId, Long userId);
}
