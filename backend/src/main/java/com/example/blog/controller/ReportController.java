package com.example.blog.controller;

import com.example.blog.dto.CreateReportRequest;
import com.example.blog.dto.ReportDTO;
import com.example.blog.model.Report;
import com.example.blog.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /* ================= USER ENDPOINTS ================= */

    /**
     * Submit a report
     * POST /api/reports
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@RequestBody @Valid CreateReportRequest request) {
        ReportDTO report = reportService.createReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /* ================= ADMIN ENDPOINTS ================= */

    /**
     * Get all reports (Admin only)
     * GET /api/reports
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Get pending reports (Admin only)
     * GET /api/reports/pending
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<ReportDTO>> getPendingReports() {
        List<ReportDTO> reports = reportService.getPendingReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Get reports by status (Admin only)
     * GET /api/reports/status/{status}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportDTO>> getReportsByStatus(
            @PathVariable @NonNull Report.ReportStatus status
    ) {
        List<ReportDTO> reports = reportService.getReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get reports against a specific user (Admin only)
     * GET /api/reports/user/{userId}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDTO>> getReportsAgainstUser(@PathVariable @NonNull Long userId) {
        List<ReportDTO> reports = reportService.getReportsAgainstUser(userId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Update report status (Admin only)
     * PATCH /api/reports/{id}/status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReportDTO> updateReportStatus(
            @PathVariable @NonNull Long id,
            @RequestParam @NonNull Report.ReportStatus status
    ) {
        ReportDTO updated = reportService.updateReportStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete report (Admin only)
     * DELETE /api/reports/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable @NonNull Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get pending report count (Admin dashboard)
     * GET /api/reports/pending/count
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending/count")
    public ResponseEntity<Map<String, Long>> getPendingCount() {
        long count = reportService.countPendingReports();
        return ResponseEntity.ok(Map.of("count", count));
    }
}
