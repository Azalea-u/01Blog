package com.example.blog.repository;

import com.example.blog.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Find all reports ordered by creation date (newest first)
     */
    List<Report> findAllByOrderByCreatedAtDesc();

    /**
     * Find reports by status, ordered by creation date (newest first)
     */
    List<Report> findByStatusOrderByCreatedAtDesc(Report.ReportStatus status);

    /**
     * Find all reports against a specific user, ordered by creation date
     */
    List<Report> findByReportedUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find all reports by a specific reporter
     */
    List<Report> findByReporterIdOrderByCreatedAtDesc(Long reporterId);

    /**
     * Find reports for a specific post
     */
    List<Report> findByReportedPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * Count reports by status
     */
    long countByStatus(Report.ReportStatus status);

    /**
     * Count reports against a user
     */
    long countByReportedUserId(Long userId);

    /**
     * Count reports for a post
     */
    long countByReportedPostId(Long postId);
}
