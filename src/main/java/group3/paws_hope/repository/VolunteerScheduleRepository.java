package group3.paws_hope.repository;

import group3.paws_hope.entity.VolunteerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VolunteerScheduleRepository extends JpaRepository<VolunteerSchedule,Long> {
    int countByShift_ShiftIdAndWorkDate(Long shiftId, LocalDate workDate);
    int countDistinctWorkDateByWeek_WeekIdAndUser_UserId(Long weekId, Long userId);
    List<VolunteerSchedule> findByWeek_Window_WindowId(Long windowId);
    boolean existsByUser_UserIdAndShift_ShiftIdAndWorkDate(Long userId, Long shiftId, LocalDate workDate);
    boolean existsByUser_UserIdAndWorkDate(Long userId, LocalDate workDate);
}
