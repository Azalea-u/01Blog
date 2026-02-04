package com.example.blog.service;

import com.example.blog.dto.CreateReportRequest;
import com.example.blog.dto.ReportDTO;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.mapper.ReportMapper;
import com.example.blog.model.Post;
import com.example.blog.model.Report;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.ReportRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;
    private final PostRepository postRepository;
    private final UserSecurity userSecurity;

    public ReportService(
            ReportRepository reportRepository,
            UserService userService,
            PostRepository postRepository,
            UserSecurity userSecurity
    ) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.postRepository = postRepository;
        this.userSecurity = userSecurity;
    }

    /**
     * Create a report
     */
    public ReportDTO createReport(@NonNull CreateReportRequest request) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to submit a report");
        }

        // Cannot report yourself
        if (currentUserId.equals(request.getReportedUserId())) {
            throw new BadRequestException("You cannot report yourself");
        }

        // Get reporter
        User reporter = userService.getUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Get reported user
        User reportedUser = userService.getUserById(request.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));

        // Create report
        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(request.getReason());
        report.setStatus(Report.ReportStatus.PENDING);

        // If reporting a specific post
        if (request.getReportedPostId() != null) {
            Post post = postRepository.findById(request.getReportedPostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            report.setReportedPost(post);
        }

        Report savedReport = reportRepository.save(report);
        return ReportMapper.toDTO(savedReport);
    }

    /**
     * Get all reports (Admin only)
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending reports (Admin only)
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> getPendingReports() {
        return reportRepository.findByStatusOrderByCreatedAtDesc(Report.ReportStatus.PENDING)
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get reports by status (Admin only)
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByStatus(@NonNull Report.ReportStatus status) {
        return reportRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get reports against a specific user (Admin only)
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsAgainstUser(@NonNull Long userId) {
        return reportRepository.findByReportedUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update report status (Admin only)
     */
    public ReportDTO updateReportStatus(@NonNull Long reportId, @NonNull Report.ReportStatus status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setStatus(status);
        Report updated = reportRepository.save(report);
        return ReportMapper.toDTO(updated);
    }

    /**
     * Delete report (Admin only)
     */
    public void deleteReport(@NonNull Long reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new ResourceNotFoundException("Report not found");
        }
        reportRepository.deleteById(reportId);
    }

    /**
     * Count pending reports (Admin dashboard)
     */
    @Transactional(readOnly = true)
    public long countPendingReports() {
        return reportRepository.countByStatus(Report.ReportStatus.PENDING);
    }
}
