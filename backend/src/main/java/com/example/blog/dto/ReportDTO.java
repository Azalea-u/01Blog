package com.example.blog.dto;

import com.example.blog.model.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private Long reporterId;
    private String reporterUsername;
    private Long reportedUserId;
    private String reportedUsername;
    private Long reportedPostId;
    private String reason;
    private Report.ReportStatus status;
    private OffsetDateTime createdAt;
}
