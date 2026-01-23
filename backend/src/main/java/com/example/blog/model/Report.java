package com.example.blog.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "reports",
    indexes = {
        @Index(columnList = "reported_user_id"),
        @Index(columnList = "status")
    }
)
public class Report {

    public enum ReportStatus {
        PENDING,
        RESOLVED,
        REJECTED
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User reporter;

    @ManyToOne
    private User reportedUser;

    @ManyToOne
    private Post reportedPost;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReportStatus status = ReportStatus.PENDING;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }

    public User getReportedUser() { return reportedUser; }
    public void setReportedUser(User reportedUser) { this.reportedUser = reportedUser; }

    public Post getReportedPost() { return reportedPost; }
    public void setReportedPost(Post reportedPost) { this.reportedPost = reportedPost; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
