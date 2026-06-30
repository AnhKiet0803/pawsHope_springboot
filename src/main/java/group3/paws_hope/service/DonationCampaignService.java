package group3.paws_hope.service;

import group3.paws_hope.dto.req.DonationCampaignReq;
import group3.paws_hope.dto.res.DonationCampaignRes;
import group3.paws_hope.entity.DonationCampaign;
import group3.paws_hope.entity.User;
import group3.paws_hope.entity.Donation;
import group3.paws_hope.repository.DonationCampaignRepository;
import group3.paws_hope.repository.DonationRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class DonationCampaignService {
    private final DonationCampaignRepository donationCampaignRepository;
    private final UserRepository userRepository;
    private final DonationRepository donationRepository;

    private BigDecimal calculateRaisedAmount(Long campaignId) {
        List<Donation> donations = donationRepository.findByCampaign_CampaignId(campaignId);
        return donations.stream()
                .filter(d -> d.getPaymentStatus() == Donation.PaymentStatus.PAID)
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<DonationCampaignRes> getAll() {
        return donationCampaignRepository.findAll().stream()
                .map(campaign -> {
                    DonationCampaignRes res = DonationCampaignRes.toJson(campaign);
                    res.setRaisedAmount(calculateRaisedAmount(campaign.getCampaignId()));
                    return res;
                })
                .toList();
    }

    public List<DonationCampaignRes> getByStatus(String status) {
        // 1. Ép chuỗi truyền vào thành chữ hoa để tránh lỗi lệch viết hoa/thường (VD: active -> ACTIVE)
        DonationCampaign.Status campaignStatus = DonationCampaign.Status.valueOf(status.toUpperCase().trim());

        return donationCampaignRepository
                .findByStatus(campaignStatus)
                .stream()
                .map(campaign -> {
                    // 2. Chuyển sang dạng DTO phản hồi
                    DonationCampaignRes res = DonationCampaignRes.toJson(campaign);

                    // 3. Tính toán số tiền thật đã quyên góp được dựa trên campaign_id từ database
                    res.setRaisedAmount(calculateRaisedAmount(campaign.getCampaignId()));

                    return res;
                })
                .toList();
    }

    public DonationCampaignRes findById(Long id) {
        DonationCampaign campaign = donationCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        return DonationCampaignRes.toJson(campaign);
    }

    public DonationCampaignRes create(DonationCampaignReq req, String email) {
        try {
            User creator = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            DonationCampaign campaign = new DonationCampaign();

            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());
            if (req.getStatus() != null) {
                campaign.setStatus(DonationCampaign.Status.valueOf(req.getStatus()));
            }

            campaign.setCreatedBy(creator);

            return DonationCampaignRes.toJson(
                    donationCampaignRepository.save(campaign)
            );
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