package group3.paws_hope.repository;

import group3.paws_hope.entity.AdoptionFollowup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdoptionFollowupRepository extends JpaRepository<AdoptionFollowup, Long> {
    List<AdoptionFollowup> findByAdoption_AdoptionId(Long adoptionId);
    List<AdoptionFollowup> findByFollowupDateAndStatus(
            LocalDate followupDate,
            AdoptionFollowup.Status status
    );
}