package group3.paws_hope.service;

import group3.paws_hope.dto.req.KennelReq;
import group3.paws_hope.dto.res.KennelRes;
import group3.paws_hope.entity.Kennel;
import group3.paws_hope.repository.KennelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KennelService {
    private final KennelRepository kennelRepository;

    public List<KennelRes> getAll() {
        return kennelRepository.findAll().stream()
                .map(KennelRes::toJson).toList();
    }

    public KennelRes findById(Long id) {
        return KennelRes.toJson(kennelRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Kennel not found")));
    }

    public KennelRes create(KennelReq req) {
        try {
            Kennel kennel = new Kennel();
            kennel.setName(req.getName());
            kennel.setCapacity(req.getCapacity());
            kennel.setDescription(req.getDescription());
            return KennelRes.toJson(kennelRepository.save(kennel));
        } catch (Exception e) {
            return null;
        }
    }

    public KennelRes update(Long id, KennelReq req) {
        try {
            Kennel kennel = kennelRepository.findById(id).orElseThrow(() -> new RuntimeException("Kennel not found"));
            kennel.setName(req.getName());
            kennel.setCapacity(req.getCapacity());
            kennel.setDescription(req.getDescription());
            return KennelRes.toJson(kennelRepository.save(kennel));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        kennelRepository.deleteById(id);
    }
}