package group3.paws_hope.service;

import group3.paws_hope.dto.req.AdoptionGuidelineReq;
import group3.paws_hope.dto.res.AdoptionGuidelineRes;
import group3.paws_hope.entity.AdoptionGuideline;
import group3.paws_hope.repository.AdoptionGuidelineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdoptionGuidelineService {

    private final AdoptionGuidelineRepository adoptionGuidelineRepositoryrepository;

    public List<AdoptionGuidelineRes> getAll() {
        return adoptionGuidelineRepositoryrepository.findAll().stream()
                .map(AdoptionGuidelineRes::toJson)
                .toList();
    }

    public AdoptionGuidelineRes findById(Long id) {

        AdoptionGuideline guideline = adoptionGuidelineRepositoryrepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guideline not found"));

        return AdoptionGuidelineRes.toJson(guideline);
    }

    public AdoptionGuidelineRes create(AdoptionGuidelineReq req) {

        try {

            AdoptionGuideline guideline = new AdoptionGuideline();

            guideline.setTitle(req.getTitle());
            guideline.setContent(req.getContent());
            guideline.setPriority(
                    req.getPriority() != null ? req.getPriority() : 0
            );

            return AdoptionGuidelineRes.toJson(adoptionGuidelineRepositoryrepository.save(guideline));

        } catch (Exception e) {
            return null;
        }
    }

    public AdoptionGuidelineRes update(Long id, AdoptionGuidelineReq req) {

        try {

            AdoptionGuideline guideline = adoptionGuidelineRepositoryrepository.findById(id)
                    .orElseThrow();

            guideline.setTitle(req.getTitle());
            guideline.setContent(req.getContent());
            guideline.setPriority(
                    req.getPriority() != null ? req.getPriority() : 0
            );

            return AdoptionGuidelineRes.toJson(adoptionGuidelineRepositoryrepository.save(guideline));

        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        adoptionGuidelineRepositoryrepository.deleteById(id);
    }
}