package group3.paws_hope.repository;

import group3.paws_hope.entity.AdoptionHandover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionHandoverRepository extends JpaRepository<AdoptionHandover, Long> {
    List<AdoptionHandover> findByAdoption_AdoptionId(Long adoptionId);
}