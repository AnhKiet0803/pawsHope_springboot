package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RescueReportReq {
    private Long userId;
    private String reporterName;

    @NotBlank(message = "Reporter phone cannot be left blank.")
    private String reporterPhone;

    @NotBlank(message = "Location cannot be left blank.")
    private String locationText;

    private String urgencyLevel;
    private String injuryType;
    private String temperament;
    private String behavior;
    private String additionalNote;
    private String imageUrl;
}