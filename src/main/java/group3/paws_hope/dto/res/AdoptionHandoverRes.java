package group3.paws_hope.dto.res;

import group3.paws_hope.entity.AdoptionHandover;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class AdoptionHandoverRes {
    private Long handoverId;
    private Long adoptionId;
    private Long handledBy;
    private LocalDateTime pickupDatetime;
    private String pickupLocation;
    private String handoverMethod;
    private String status;
    private Boolean adopterConfirmed;
    private String itemsGiven;
    private String handoverPhotoUrl;
    private String completionNote;
    private LocalDateTime completedAt;
    private Timestamp createdAt;

    public static AdoptionHandoverRes toJson(AdoptionHandover handover) {
        return new AdoptionHandoverRes(
                handover.getHandoverId(),
                handover.getAdoption().getAdoptionId(),
                handover.getHandledBy() != null ? handover.getHandledBy().getUserId() : null,
                handover.getPickupDatetime(),
                handover.getPickupLocation(),
                handover.getHandoverMethod().name(),
                handover.getStatus().name(),
                handover.getAdopterConfirmed(),
                handover.getItemsGiven(),
                handover.getHandoverPhotoUrl(),
                handover.getCompletionNote(),
                handover.getCompletedAt(),
                handover.getCreatedAt()
        );
    }
}