package group3.paws_hope.repository;

import group3.paws_hope.entity.AdoptionMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdoptionMeetingRepository extends JpaRepository<AdoptionMeeting, Long> {
    List<AdoptionMeeting> findByAdoption_AdoptionId(Long adoptionId);
}