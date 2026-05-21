package group3.paws_hope.repository;

import group3.paws_hope.entity.RescueReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RescueReportRepository extends JpaRepository<RescueReport, Long> {
    Optional<RescueReport> findByTrackingCode(String trackingCode);
    List<RescueReport> findByStatus(RescueReport.Status status);
}