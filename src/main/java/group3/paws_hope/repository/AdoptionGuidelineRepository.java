package group3.paws_hope.repository;

import group3.paws_hope.entity.AdoptionGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionGuidelineRepository extends JpaRepository<AdoptionGuideline,Long> {
}
