package group3.paws_hope.service;

import group3.paws_hope.dto.req.DonationReq;
import group3.paws_hope.dto.res.DonationRes;
import group3.paws_hope.entity.Donation;
import group3.paws_hope.entity.DonationCampaign;
import group3.paws_hope.entity.Order;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.DonationCampaignRepository;
import group3.paws_hope.repository.DonationRepository;
import group3.paws_hope.repository.OrderRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final DonationCampaignRepository donationCampaignRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<DonationRes> getAll() {
        return donationRepository.findAll().stream()
                .map(DonationRes::toJson)
                .toList();
    }

    public DonationRes findById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        return DonationRes.toJson(donation);
    }

    public List<DonationRes> getByCampaignId(Long campaignId) {
        return donationRepository.findByCampaign_CampaignId(campaignId).stream()
                .map(DonationRes::toJson)
                .toList();
    }

    public List<DonationRes> getByUserId(Long userId) {
        return donationRepository.findByUser_UserId(userId).stream()
                .map(DonationRes::toJson)
                .toList();
    }

    public DonationRes create(DonationReq req) {
        try {
            DonationCampaign campaign = donationCampaignRepository.findById(req.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            if (campaign.getStatus() != DonationCampaign.Status.ONGOING) {
                throw new RuntimeException("Campaign is not ongoing");
            }

            Donation donation = new Donation();

            donation.setCampaign(campaign);
            donation.setAmount(req.getAmount());
            donation.setDonationType(Donation.DonationType.valueOf(req.getDonationType()));

            if (req.getUserId() != null) {
                User user = userRepository.findById(req.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                donation.setUser(user);
                donation.setDonorNameManual(user.getFullName());
            } else {
                donation.setDonorNameManual(req.getDonorNameManual() != null ? req.getDonorNameManual() : "GUEST");
            }

            if (req.getPaymentStatus() != null) {
                donation.setPaymentStatus(Donation.PaymentStatus.valueOf(req.getPaymentStatus()));
            }

            if (req.getSourceOrderId() != null) {
                Order order = orderRepository.findById(req.getSourceOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found"));

                donation.setSourceOrder(order);
            }

            return DonationRes.toJson(donationRepository.save(donation));
        } catch (Exception e) {
            return null;
        }
    }

    public DonationRes updatePaymentStatus(Long id, String status) {
        try {
            Donation donation = donationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Donation not found"));

            donation.setPaymentStatus(Donation.PaymentStatus.valueOf(status));

            return DonationRes.toJson(donationRepository.save(donation));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        donationRepository.deleteById(id);
    }
}