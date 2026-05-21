package group3.paws_hope.dto.res;

import group3.paws_hope.entity.RescueReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class RescueReportRes {
    private Long reportId;
    private Long userId;
    private String reporterName;
    private String reporterPhone;
    private String locationText;
    private String urgencyLevel;
    private String injuryType;
    private String temperament;
    private String behavior;
    private String additionalNote;
    private String imageUrl;
    private String status;
    private Long assignedTo;
    private String trackingCode;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static RescueReportRes toJson(RescueReport report) {
        return new RescueReportRes(
                report.getReportId(),
                report.getUser() != null ? report.getUser().getUserId() : null,
                report.getReporterName(),
                report.getReporterPhone(),
                report.getLocationText(),
                report.getUrgencyLevel().name(),
                report.getInjuryType().name(),
                report.getTemperament().name(),
                report.getBehavior().name(),
                report.getAdditionalNote(),
                report.getImageUrl(),
                report.getStatus().name(),
                report.getAssignedTo() != null ? report.getAssignedTo().getUserId() : null,
                report.getTrackingCode(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}