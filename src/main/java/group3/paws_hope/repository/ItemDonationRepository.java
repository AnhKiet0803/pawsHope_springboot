package group3.paws_hope.repository;

import group3.paws_hope.entity.ItemDonation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDonationRepository extends JpaRepository<ItemDonation, Long> {
    List<ItemDonation> findByUser_UserId(Long userId);
    List<ItemDonation> findByStatus(ItemDonation.Status status);
    List<ItemDonation> findByCategory(ItemDonation.Category category);
}