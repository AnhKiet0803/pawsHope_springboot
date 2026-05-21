package group3.paws_hope.repository;

import group3.paws_hope.entity.AdoptionMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionMeetingRepository extends JpaRepository<AdoptionMeeting, Long> {
    List<AdoptionMeeting> findByAdoption_AdoptionId(Long adoptionId);
}