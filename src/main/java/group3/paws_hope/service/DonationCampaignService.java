package group3.paws_hope.service;

import group3.paws_hope.dto.req.DonationCampaignReq;
import group3.paws_hope.dto.res.DonationCampaignRes;
import group3.paws_hope.entity.DonationCampaign;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.DonationCampaignRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonationCampaignService {
    private final DonationCampaignRepository donationCampaignRepository;
    private final UserRepository userRepository;

    public List<DonationCampaignRes> getAll() {
        return donationCampaignRepository.findAll().stream()
                .map(DonationCampaignRes::toJson)
                .toList();
    }

    public List<DonationCampaignRes> getByStatus(String status) {
        return donationCampaignRepository
                .findByStatus(DonationCampaign.Status.valueOf(status))
                .stream()
                .map(DonationCampaignRes::toJson)
                .toList();
    }

    public DonationCampaignRes findById(Long id) {
        DonationCampaign campaign = donationCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        return DonationCampaignRes.toJson(campaign);
    }

    public DonationCampaignRes create(DonationCampaignReq req) {
        try {
            DonationCampaign campaign = new DonationCampaign();

            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());

            if (req.getStatus() != null) {
                campaign.setStatus(DonationCampaign.Status.valueOf(req.getStatus()));
            }

            if (req.getCreatedBy() != null) {
                User creator = userRepository.findById(req.getCreatedBy())
                        .orElseThrow(() -> new RuntimeException("Creator not found"));
                campaign.setCreatedBy(creator);
            }

            return DonationCampaignRes.toJson(donationCampaignRepository.save(campaign));
        } catch (Exception e) {
            return null;
        }
    }

    public DonationCampaignRes update(Long id, DonationCampaignReq req) {
        try {
            DonationCampaign campaign = donationCampaignRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());

            if (req.getStatus() != null) {
                campaign.setStatus(DonationCampaign.Status.valueOf(req.getStatus()));
            }

            return DonationCampaignRes.toJson(donationCampaignRepository.save(campaign));
        } catch (Exception e) {
            return null;
        }
    }

    public DonationCampaignRes updateStatus(Long id, String status) {
        try {
            DonationCampaign campaign = donationCampaignRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            campaign.setStatus(DonationCampaign.Status.valueOf(status));

            return DonationCampaignRes.toJson(donationCampaignRepository.save(campaign));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        donationCampaignRepository.deleteById(id);
    }
}