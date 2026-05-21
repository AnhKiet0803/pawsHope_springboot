package group3.paws_hope.service;

import group3.paws_hope.dto.req.ItemDonationReq;
import group3.paws_hope.dto.res.ItemDonationRes;
import group3.paws_hope.entity.ItemDonation;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.ItemDonationRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemDonationService {
    private final ItemDonationRepository itemDonationRepository;
    private final UserRepository userRepository;

    public List<ItemDonationRes> getAll() {
        return itemDonationRepository.findAll().stream()
                .map(ItemDonationRes::toJson)
                .toList();
    }

    public ItemDonationRes findById(Long id) {
        ItemDonation itemDonation = itemDonationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item donation not found"));

        return ItemDonationRes.toJson(itemDonation);
    }

    public List<ItemDonationRes> getByUserId(Long userId) {
        return itemDonationRepository.findByUser_UserId(userId).stream()
                .map(ItemDonationRes::toJson)
                .toList();
    }

    public List<ItemDonationRes> getByStatus(String status) {
        return itemDonationRepository.findByStatus(
                        ItemDonation.Status.valueOf(status)
                ).stream()
                .map(ItemDonationRes::toJson)
                .toList();
    }

    public List<ItemDonationRes> getByCategory(String category) {
        return itemDonationRepository.findByCategory(
                        ItemDonation.Category.valueOf(category)
                ).stream()
                .map(ItemDonationRes::toJson)
                .toList();
    }

    public ItemDonationRes create(ItemDonationReq req) {
        try {
            ItemDonation itemDonation = new ItemDonation();
            if (req.getUserId() != null) {
                User user = userRepository.findById(req.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                itemDonation.setUser(user);
                itemDonation.setDonorNameManual(user.getFullName());
            } else {
                itemDonation.setDonorNameManual(
                        req.getDonorNameManual() != null ? req.getDonorNameManual() : "GUEST"
                );
            }
            itemDonation.setItemName(req.getItemName());
            itemDonation.setNote(req.getNote());
            itemDonation.setQuantity(req.getQuantity());
            if (req.getCategory() != null) {
                itemDonation.setCategory(ItemDonation.Category.valueOf(req.getCategory()));
            }
            if (req.getStatus() != null) {
                itemDonation.setStatus(ItemDonation.Status.valueOf(req.getStatus()));
            }
            if (req.getReceivedBy() != null) {
                User receiver = userRepository.findById(req.getReceivedBy())
                        .orElseThrow(() -> new RuntimeException("Receiver not found"));

                itemDonation.setReceivedBy(receiver);
            }
            return ItemDonationRes.toJson(
                    itemDonationRepository.save(itemDonation)
            );
        } catch (Exception e) {
            return null;
        }
    }

    public ItemDonationRes updateStatus(Long id, String status, Long receivedBy) {
        try {
            ItemDonation itemDonation = itemDonationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item donation not found"));

            itemDonation.setStatus(ItemDonation.Status.valueOf(status));
            if (receivedBy != null) {
                User receiver = userRepository.findById(receivedBy)
                        .orElseThrow(() -> new RuntimeException("Receiver not found"));

                itemDonation.setReceivedBy(receiver);
            }
            return ItemDonationRes.toJson(itemDonationRepository.save(itemDonation));
        } catch (Exception e) {
            return null;
        }
    }

    public ItemDonationRes update(Long id, ItemDonationReq req) {
        try {
            ItemDonation itemDonation = itemDonationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item donation not found"));

            itemDonation.setItemName(req.getItemName());
            itemDonation.setNote(req.getNote());
            itemDonation.setQuantity(req.getQuantity());

            if (req.getCategory() != null) {
                itemDonation.setCategory(ItemDonation.Category.valueOf(req.getCategory()));
            }
            return ItemDonationRes.toJson(itemDonationRepository.save(itemDonation));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        itemDonationRepository.deleteById(id);
    }
}