import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO, UserAdminDTO, ProfileDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getMe(): Observable<UserDTO>                               { return this.http.get<UserDTO>('/api/users/me'); }
  getUserById(id: number): Observable<UserDTO | ProfileDTO>  { return this.http.get<UserDTO | ProfileDTO>(`/api/users/${id}`); }
  getProfile(username: string): Observable<ProfileDTO>       { return this.http.get<ProfileDTO>(`/api/users/profile/${username}`); }

  updateUser(id: number, data: Partial<{ email: string; bio: string }>): Observable<UserDTO> {
    return this.http.put<UserDTO>(`/api/users/${id}`, data);
  }
  changePassword(id: number, currentPassword: string, newPassword: string): Observable<void> {
    return this.http.put<void>(`/api/users/${id}/password`, { currentPassword, newPassword });
  }

  // Admin
  getAllUsers(): Observable<UserAdminDTO[]>        { return this.http.get<UserAdminDTO[]>('/api/users'); }
  banUser(id: number): Observable<void>           { return this.http.patch<void>(`/api/users/${id}/ban`, {}); }
  unbanUser(id: number): Observable<void>         { return this.http.patch<void>(`/api/users/${id}/unban`, {}); }
  deleteUser(id: number): Observable<void>        { return this.http.delete<void>(`/api/users/${id}`); }
}
