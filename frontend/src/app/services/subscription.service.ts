import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProfileDTO, SubscriptionStats } from '../models';

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  constructor(private http: HttpClient) {}

  subscribe(userId: number): Observable<unknown>   { return this.http.post(`/api/subscriptions/${userId}`, {}); }
  unsubscribe(userId: number): Observable<unknown> { return this.http.delete(`/api/subscriptions/${userId}`); }

  checkStatus(userId: number): Observable<{ subscribed: boolean }> {
    return this.http.get<{ subscribed: boolean }>(`/api/subscriptions/${userId}/status`);
  }
  getFollowing(userId: number): Observable<ProfileDTO[]> {
    return this.http.get<ProfileDTO[]>(`/api/subscriptions/${userId}/following`);
  }
  getFollowers(userId: number): Observable<ProfileDTO[]> {
    return this.http.get<ProfileDTO[]>(`/api/subscriptions/${userId}/followers`);
  }
  getStats(userId: number): Observable<SubscriptionStats> {
    return this.http.get<SubscriptionStats>(`/api/subscriptions/${userId}/stats`);
  }
}
