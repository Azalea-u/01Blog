import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotificationDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<NotificationDTO[]>          { return this.http.get<NotificationDTO[]>('/api/notifications'); }
  getUnread(): Observable<NotificationDTO[]>       { return this.http.get<NotificationDTO[]>('/api/notifications/unread'); }
  getUnreadCount(): Observable<{ count: number }>  { return this.http.get<{ count: number }>('/api/notifications/unread/count'); }
  markAsRead(id: number): Observable<unknown>      { return this.http.patch(`/api/notifications/${id}/read`, {}); }
  markAllAsRead(): Observable<unknown>             { return this.http.patch('/api/notifications/read-all', {}); }
  delete(id: number): Observable<void>             { return this.http.delete<void>(`/api/notifications/${id}`); }
  deleteAll(): Observable<unknown>                 { return this.http.delete('/api/notifications'); }
}
