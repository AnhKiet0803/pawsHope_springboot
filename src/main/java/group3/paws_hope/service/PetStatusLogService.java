package group3.paws_hope.service;

import group3.paws_hope.dto.req.PetStatusLogReq;
import group3.paws_hope.dto.res.PetStatusLogRes;
import group3.paws_hope.entity.Pet;
import group3.paws_hope.entity.PetStatusLog;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.PetRepository;
import group3.paws_hope.repository.PetStatusLogRepository;
import group3.paws_hope.repository.UserRepository;
import jakarta.transaction.Transactional;


import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PetStatusLogService {
    private final PetStatusLogRepository petStatusLogRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetStatusLogService(PetStatusLogRepository petStatusLogRepository,
                               PetRepository petRepository,
                               UserRepository userRepository) {
        this.petStatusLogRepository = petStatusLogRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

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
    // Thêm @Transactional vì hàm này thay đổi dữ liệu của 2 bảng cùng lúc
    @Transactional
    public PetStatusLogRes create(PetStatusLogReq req) {
        try {
            // Tìm bé thú cưng
            Pet pet = petRepository.findById(req.getPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));

            // Tìm Admin đang thực hiện thao tác
            User user = null;
            if (req.getUpdatedBy() != null) {
                user = userRepository.findById(req.getUpdatedBy())
                        .orElseThrow(() -> new RuntimeException("Admin User not found"));
            }

            // 1. Tạo và lưu Log mới
            PetStatusLog log = new PetStatusLog();
            log.setPet(pet);
            log.setOldStatus(Pet.Status.valueOf(req.getOldStatus()));
            log.setNewStatus(Pet.Status.valueOf(req.getNewStatus()));
            pet.setStatus(Pet.Status.valueOf(req.getNewStatus()));
            log.setNote(req.getNote());
            log.setUpdatedBy(user);
            PetStatusLog savedLog = petStatusLogRepository.save(log);

            // 2. Cập nhật lại trạng thái mới nhất cho thú cưng
            pet.setStatus(Pet.Status.valueOf(req.getNewStatus()));
            petRepository.save(pet);

            // Trả về JSON để Frontend cập nhật danh sách
            return PetStatusLogRes.toJson(savedLog);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm log: " + e.getMessage());
        }
    }
}