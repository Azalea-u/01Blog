package com.example.blog.mapper;

import com.example.blog.dto.ReportDTO;
import com.example.blog.model.Report;

public final class ReportMapper {

    private ReportMapper() {}

    public static ReportDTO toDTO(Report report) {
        if (report == null) return null;

        return new ReportDTO(
            report.getId(),
            report.getReporter().getId(),
            report.getReporter().getUsername(),
            report.getReportedUser().getId(),
            report.getReportedUser().getUsername(),
            report.getReportedPost() != null ? report.getReportedPost().getId() : null,
            report.getReason(),
            report.getStatus(),
            report.getCreatedAt()
        );
    }
}
