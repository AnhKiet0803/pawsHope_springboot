package group3.paws_hope.service;

import group3.paws_hope.dto.res.PetStatusLogRes;
import group3.paws_hope.repository.PetStatusLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetStatusLogService {
    private final PetStatusLogRepository petStatusLogRepository;

    public List<PetStatusLogRes> getAll() {
        return petStatusLogRepository.findAll().stream()
                .map(PetStatusLogRes::toJson)
                .toList();
    }

    public List<PetStatusLogRes> getByPetId(Long petId) {
        return petStatusLogRepository.findByPet_PetId(petId).stream()
                .map(PetStatusLogRes::toJson)
                .toList();
    }
}