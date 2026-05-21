package group3.paws_hope.repository;

import group3.paws_hope.entity.PetStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetStatusLogRepository extends JpaRepository<PetStatusLog, Long> {
    List<PetStatusLog> findByPet_PetId(Long petId);
}