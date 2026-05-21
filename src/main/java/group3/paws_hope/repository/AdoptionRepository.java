package group3.paws_hope.repository;

import group3.paws_hope.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    Optional<Adoption> findByApplicationCode(String applicationCode);
    List<Adoption> findByUser_UserId(Long userId);
    List<Adoption> findByPet_PetId(Long petId);
}