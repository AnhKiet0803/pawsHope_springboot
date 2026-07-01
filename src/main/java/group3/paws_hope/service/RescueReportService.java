package group3.paws_hope.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import group3.paws_hope.dto.req.RescueReportReq;
import group3.paws_hope.dto.res.RescueReportRes;
import group3.paws_hope.entity.Notification;
import group3.paws_hope.entity.RescueReport;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.NotificationRepository;
import group3.paws_hope.repository.RescueReportRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RescueReportService {
    private final RescueReportRepository rescueReportRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final Cloudinary cloudinary;

    public List<RescueReportRes> getAll() {
        return rescueReportRepository.findAll().stream()
                .map(RescueReportRes::toJson)
                .toList();
    }

    public List<RescueReportRes> getByUserId(Long userId) {
        return rescueReportRepository.findByUser_UserIdOrderByReportIdDesc(userId).stream()
                .map(RescueReportRes::toJson)
                .toList();
    }

    public RescueReportRes findById(Long id) {
        RescueReport report = rescueReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue report not found"));

        return RescueReportRes.toJson(report);
    }

    public RescueReportRes getByTrackingCode(String trackingCode) {
        RescueReport report = rescueReportRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new RuntimeException("Tracking code not found"));

        return RescueReportRes.toJson(report);
    }

    public RescueReportRes create(RescueReportReq req, MultipartFile image) {
        validateRequiredFields(req, image);

        try {
            RescueReport rescueReport = buildReport(req);
            rescueReport.setImageUrl(uploadImage(image));

            RescueReport saved = rescueReportRepository.save(rescueReport);
            notifyAdminsAndVolunteers(saved);

            return RescueReportRes.toJson(saved);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Submit rescue report failed: " + e.getMessage());
        }
    }

    public RescueReportRes accept(Long reportId, String email) {
        try {
            RescueReport rescueReport = rescueReportRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Rescue report not found"));

            if (rescueReport.getAssignedTo() != null) {
                throw new RuntimeException("This rescue report has already been accepted");
            }

            if (rescueReport.getStatus() != RescueReport.Status.PENDING) {
                throw new RuntimeException("Only pending rescue reports can be accepted");
            }

            User receiver = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (receiver.getRole() != User.Role.ADMIN
                    && receiver.getRole() != User.Role.VOLUNTEER) {
                throw new RuntimeException("Only admin or volunteer can accept rescue reports");
            }

            rescueReport.setAssignedTo(receiver);
            rescueReport.setStatus(RescueReport.Status.IN_PROGRESS);

            return RescueReportRes.toJson(rescueReportRepository.save(rescueReport));

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Accept rescue report failed");
        }
    }

    public RescueReportRes updateStatus(Long id, String status) {
        try {
            RescueReport rescueReport = rescueReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rescue report not found"));
            rescueReport.setStatus(RescueReport.Status.valueOf(status));

            return RescueReportRes.toJson(rescueReportRepository.save(rescueReport));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Update status failed");
        }
    }

    public void delete(Long id) {
        rescueReportRepository.deleteById(id);
    }

    private void validateRequiredFields(RescueReportReq req, MultipartFile image) {
        if (req.getAdditionalNote() == null || req.getAdditionalNote().isBlank()) {
            throw new RuntimeException("Additional details cannot be left blank.");
        }
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Rescue photo is required.");
        }
        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new RuntimeException("Uploaded file must be an image.");
        }
    }

    private RescueReport buildReport(RescueReportReq req) {
        RescueReport rescueReport = new RescueReport();
        if (req.getUserId() != null) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            rescueReport.setUser(user);
        }

        rescueReport.setReporterName(req.getReporterName());
        rescueReport.setReporterPhone(req.getReporterPhone());
        rescueReport.setLocationText(req.getLocationText());
        rescueReport.setAdditionalNote(req.getAdditionalNote().trim());
        rescueReport.setStatus(RescueReport.Status.PENDING);
        rescueReport.setTrackingCode(generateTrackingCode());

        if (req.getUrgencyLevel() != null) {
            rescueReport.setUrgencyLevel(RescueReport.UrgencyLevel.valueOf(req.getUrgencyLevel()));
        }

        if (req.getInjuryType() != null) {
            rescueReport.setInjuryType(RescueReport.InjuryType.valueOf(req.getInjuryType()));
        }

        if (req.getTemperament() != null) {
            rescueReport.setTemperament(RescueReport.Temperament.valueOf(req.getTemperament()));
        }

        if (req.getBehavior() != null) {
            rescueReport.setBehavior(RescueReport.Behavior.valueOf(req.getBehavior()));
        }

        return rescueReport;
    }

    private String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "pawshope_rescue")
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    private void notifyAdminsAndVolunteers(RescueReport report) {
        List<User> receivers = userRepository.findByRoleIn(List.of(User.Role.ADMIN, User.Role.VOLUNTEER));
        for (User receiver : receivers) {
            Notification notification = new Notification();
            notification.setUser(receiver);
            notification.setMessage("New rescue report: " + report.getLocationText());
            notification.setType(Notification.Type.RESCUE_ASSIGNED);
            notification.setRelatedId(report.getReportId());
            notification.setIsRead(false);
            notificationRepository.save(notification);
        }
    }

    private String generateTrackingCode() {
        return "RR" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
