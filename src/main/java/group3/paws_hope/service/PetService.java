package group3.paws_hope.service;

import group3.paws_hope.dto.req.PetReq;
import group3.paws_hope.dto.req.PetStatusLogReq;
import group3.paws_hope.dto.res.PetRes;
import group3.paws_hope.entity.*;
import group3.paws_hope.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final KennelRepository kennelRepository;
    private final RescueReportRepository rescueReportRepository;
    private final UserRepository userRepository;
    private final PetStatusLogRepository petStatusLogRepository;

    public List<PetRes> getAll() {
        return petRepository.findAll().stream()
                .map(PetRes::toJson)
                .toList();
    }

    public PetRes getById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return PetRes.toJson(pet);
    }

    public PetRes getByPetCode(String petCode) {
        Pet pet = petRepository.findByPetCode(petCode)
                .orElseThrow(() -> new RuntimeException("Pet code not found"));

        return PetRes.toJson(pet);
    }

    public PetRes create(PetReq req) {
        try {
            Pet pet = new Pet();
            pet.setPetCode(req.getPetCode());
            pet.setName(req.getName());
            pet.setSpecies(Pet.Species.valueOf(req.getSpecies()));
            if (req.getGender() != null) {
                pet.setGender(Pet.Gender.valueOf(req.getGender()));
            }

            pet.setBreed(req.getBreed());
            pet.setAgeMonths(req.getAgeMonths());
            pet.setWeightKg(req.getWeightKg());
            if (req.getHealthStatus() != null) {
                pet.setHealthStatus(Pet.HealthStatus.valueOf(req.getHealthStatus()));
            }

            pet.setPersonality(req.getPersonality());

            if (req.getStatus() != null) {
                pet.setStatus(Pet.Status.valueOf(req.getStatus()));
            }

            pet.setImageUrl(req.getImageUrl());
            pet.setIntakeDate(req.getIntakeDate());
            pet.setDescription(req.getDescription());

            if (req.getKennelId() != null) {
                Kennel kennel = kennelRepository.findById(req.getKennelId())
                        .orElseThrow(() -> new RuntimeException("Kennel not found"));
                pet.setKennel(kennel);
            }

            if (req.getFromReportId() != null) {
                RescueReport rescueReport = rescueReportRepository.findById(req.getFromReportId())
                        .orElseThrow(() -> new RuntimeException("Rescue report not found"));
                pet.setFromReport(rescueReport);
            }

            return PetRes.toJson(petRepository.save(pet));
        } catch (Exception e) {
            return null;
        }
    }

    public PetRes update(Long id, PetReq req) {
        try {
            Pet pet = petRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pet not found"));
            pet.setName(req.getName());
            pet.setSpecies(Pet.Species.valueOf(req.getSpecies()));
            pet.setBreed(req.getBreed());
            pet.setAgeMonths(req.getAgeMonths());
            pet.setWeightKg(req.getWeightKg());
            pet.setPersonality(req.getPersonality());
            pet.setImageUrl(req.getImageUrl());
            pet.setIntakeDate(req.getIntakeDate());
            pet.setDescription(req.getDescription());

            if (req.getGender() != null) {
                pet.setGender(Pet.Gender.valueOf(req.getGender()));
            }

            if (req.getHealthStatus() != null) {
                pet.setHealthStatus(Pet.HealthStatus.valueOf(req.getHealthStatus()));
            }

            if (req.getStatus() != null) {
                pet.setStatus(Pet.Status.valueOf(req.getStatus()));
            }

            if (req.getKennelId() != null) {
                Kennel kennel = kennelRepository.findById(req.getKennelId())
                        .orElseThrow(() -> new RuntimeException("Kennel not found"));
                pet.setKennel(kennel);
            }
            return PetRes.toJson(petRepository.save(pet));

        } catch (Exception e) {
            return null;
        }
    }

    public PetRes updateStatus(Long petId, PetStatusLogReq req) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
            User user = userRepository.findById(req.getUpdatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Pet.Status oldStatus = pet.getStatus();
            Pet.Status newStatus = Pet.Status.valueOf(req.getNewStatus());

            pet.setStatus(newStatus);

            Pet savedPet = petRepository.save(pet);

            PetStatusLog petStatusLog = new PetStatusLog();
            petStatusLog.setPet(savedPet);
            petStatusLog.setOldStatus(oldStatus);
            petStatusLog.setNewStatus(newStatus);
            petStatusLog.setNote(req.getNote());
            petStatusLog.setUpdatedBy(user);
            petStatusLogRepository.save(petStatusLog);

            return PetRes.toJson(savedPet);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}