package group3.paws_hope.repository;

import group3.paws_hope.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findByPetCode(String petCode);

    boolean existsByPetCode(String petCode);
}