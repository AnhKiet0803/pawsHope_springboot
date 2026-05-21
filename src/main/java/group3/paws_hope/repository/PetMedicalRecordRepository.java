package group3.paws_hope.repository;

import group3.paws_hope.entity.PetMedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetMedicalRecordRepository extends JpaRepository<PetMedicalRecord, Long> {
    List<PetMedicalRecord> findByPet_PetId(Long petId);
}