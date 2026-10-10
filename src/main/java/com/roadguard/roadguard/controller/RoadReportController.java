
package com.roadguard.roadguard.controller;

import com.roadguard.roadguard.entity.RoadReport;
import com.roadguard.roadguard.service.RoadReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class RoadReportController {

    private final RoadReportService roadReportService;

    public RoadReportController(RoadReportService roadReportService) {
        this.roadReportService = roadReportService;
    }

    @PostMapping
    public RoadReport createReport(@RequestBody RoadReport report) {
        return roadReportService.createReport(report);
    }

    @GetMapping
    public List<RoadReport> getAllReports() {
        return roadReportService.getAllReports();
    }

    @PostMapping("/ai")
    public RoadReport saveAIReport(@RequestBody Map<String, Object> data) {
        try {
            String damageType = (String) data.get("damageType");
            Double confidence = Double.valueOf(
                    data.get("confidence").toString());
            Integer detectionCount = Integer.valueOf(
                    data.get("detectionCount").toString());
            String imageUrl = (String) data.get("imageUrl");
            String location = (String) data.get("location");

            return roadReportService.saveAIReport(
                    damageType,
                    confidence,
                    detectionCount,
                    imageUrl,
                    location
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid AI report data"
            );
        }
    }
    
    @PatchMapping("/{id}/resolve")
    public RoadReport resolveReport(@PathVariable Long id) {
        return roadReportService.resolveReport(id);
    }


    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            Path uploadPath = Paths.get("uploads");
            Files.createDirectories(uploadPath);

            String filename = java.util.UUID.randomUUID()
                    + "_" + java.nio.file.Paths.get(
                            image.getOriginalFilename() == null
                                    ? "image.jpg"
                                    : image.getOriginalFilename())
                    .getFileName().toString();

            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, image.getBytes());

            return "Image uploaded successfully: " + filePath;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image upload failed"
            );
        }
    }
}
