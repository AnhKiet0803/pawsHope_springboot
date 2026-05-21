package group3.paws_hope.repository;

import group3.paws_hope.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByCampaign_CampaignId(Long campaignId);
    List<Donation> findByUser_UserId(Long userId);
}