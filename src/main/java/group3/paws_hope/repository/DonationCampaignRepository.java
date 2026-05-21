package group3.paws_hope.repository;

import group3.paws_hope.entity.DonationCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationCampaignRepository extends JpaRepository<DonationCampaign, Long> {
    List<DonationCampaign> findByStatus(DonationCampaign.Status status);
}