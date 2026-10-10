package com.roadguard.roadguard.repository;

import com.roadguard.roadguard.entity.RoadReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadReportRepository extends JpaRepository<RoadReport, Long> {
}