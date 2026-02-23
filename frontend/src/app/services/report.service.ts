import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReportDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class ReportService {
  constructor(private http: HttpClient) {}

  createReport(reportedUserId: number, reason: string, reportedPostId?: number): Observable<ReportDTO> {
    const body: Record<string, unknown> = { reportedUserId, reason };
    if (reportedPostId) body['reportedPostId'] = reportedPostId;
    return this.http.post<ReportDTO>('/api/reports', body);
  }

  getAllReports(): Observable<ReportDTO[]>     { return this.http.get<ReportDTO[]>('/api/reports'); }
  getPendingReports(): Observable<ReportDTO[]>{ return this.http.get<ReportDTO[]>('/api/reports/pending'); }
  getPendingCount(): Observable<{ count: number }> { return this.http.get<{ count: number }>('/api/reports/pending/count'); }

  updateStatus(id: number, status: string): Observable<ReportDTO> {
    return this.http.patch<ReportDTO>(`/api/reports/${id}/status?status=${status}`, {});
  }
  deleteReport(id: number): Observable<void>  { return this.http.delete<void>(`/api/reports/${id}`); }
}
