
package com.roadguard.roadguard.service;

import com.roadguard.roadguard.entity.RoadReport;
import com.roadguard.roadguard.repository.RoadReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoadReportService {

    private final RoadReportRepository roadReportRepository;

    public RoadReportService(RoadReportRepository roadReportRepository) {
        this.roadReportRepository = roadReportRepository;
    }

    public RoadReport createReport(RoadReport report) {
        return roadReportRepository.save(report);
    }

    public List<RoadReport> getAllReports() {
        return roadReportRepository.findAll();
    }

    public RoadReport saveAIReport(
            String damageType,
            Double confidence,
            Integer detectionCount,
            String imageUrl,
            String location) {

        RoadReport report = new RoadReport();

        report.setDamageType(damageType);
        report.setConfidence(confidence);
        report.setDetectionCount(detectionCount);
        report.setImageUrl(imageUrl);
        report.setLocation(location);
        report.setDescription("AI-detected road damage");

        if (damageType != null
                && damageType.toLowerCase().contains("pothole")) {
            report.setSeverity("HIGH");
        } else if (detectionCount != null && detectionCount >= 3) {
            report.setSeverity("MEDIUM");
        } else {
            report.setSeverity("LOW");
        }

        report.setStatus("REPORTED");

        return roadReportRepository.save(report);
    }

    public RoadReport resolveReport(Long id) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Report not found with ID: " + id
                ));

        report.setStatus("RESOLVED");

        return roadReportRepository.save(report);
    }
}
