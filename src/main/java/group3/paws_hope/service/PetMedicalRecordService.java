package group3.paws_hope.service;

import group3.paws_hope.dto.req.PetMedicalRecordReq;
import group3.paws_hope.dto.res.PetMedicalRecordRes;
import group3.paws_hope.entity.Pet;
import group3.paws_hope.entity.PetMedicalRecord;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.PetMedicalRecordRepository;
import group3.paws_hope.repository.PetRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetMedicalRecordService {

    private final PetMedicalRecordRepository petMedicalRecordRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public List<PetMedicalRecordRes> getAll() {
        return petMedicalRecordRepository.findAll().stream()
                .map(PetMedicalRecordRes::toJson)
                .toList();
    }

    public List<PetMedicalRecordRes> getByPetId(Long petId) {
        return petMedicalRecordRepository.findByPet_PetId(petId).stream()
                .map(PetMedicalRecordRes::toJson)
                .toList();
    }

    public PetMedicalRecordRes getById(Long id) {
        PetMedicalRecord record = petMedicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        return PetMedicalRecordRes.toJson(record);
    }

    public PetMedicalRecordRes create(PetMedicalRecordReq req) {
        try {
            Pet pet = petRepository.findById(req.getPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));
            PetMedicalRecord record = new PetMedicalRecord();
            record.setPet(pet);
            record.setRecordType(PetMedicalRecord.RecordType.valueOf(req.getRecordType()));
            record.setRecordDate(req.getRecordDate());
            record.setNextDueDate(req.getNextDueDate());
            record.setDescription(req.getDescription());
            if (req.getCreatedBy() != null) {
                User user = userRepository.findById(req.getCreatedBy())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                record.setCreatedBy(user);
            }
            return PetMedicalRecordRes.toJson(petMedicalRecordRepository.save(record));
        } catch (Exception e) {
            return null;
        }
    }

    public PetMedicalRecordRes update(Long id, PetMedicalRecordReq req) {
        try {
            PetMedicalRecord record = petMedicalRecordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Medical record not found"));
            record.setRecordType(PetMedicalRecord.RecordType.valueOf(req.getRecordType()));
            record.setRecordDate(req.getRecordDate());
            record.setNextDueDate(req.getNextDueDate());
            record.setDescription(req.getDescription());

            return PetMedicalRecordRes.toJson(petMedicalRecordRepository.save(record));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        petMedicalRecordRepository.deleteById(id);
    }
}