package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdoptionHandoverReq {
    @NotNull(message = "Adoption id cannot be null.")
    private Long adoptionId;

    private Long handledBy;

    @NotNull(message = "Pickup datetime cannot be null.")
    private LocalDateTime pickupDatetime;

    private String pickupLocation;
    private String handoverMethod;
    private String status;
    private Boolean adopterConfirmed;
    private String itemsGiven;
    private String handoverPhotoUrl;
    private String completionNote;
}