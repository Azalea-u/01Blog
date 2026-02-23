import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap, Observable } from 'rxjs';
import { AuthResponse, LoginRequest, RegisterRequest, UserDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'blog_token';
  private readonly USER_KEY  = 'blog_user';

  private _currentUser = signal<UserDTO | null>(this.loadUser());

  currentUser = this._currentUser.asReadonly();
  isLoggedIn  = computed(() => this._currentUser() !== null);
  isAdmin     = computed(() => this._currentUser()?.role === 'ADMIN');

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', req).pipe(tap(r => this.setAuth(r)));
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/register', req).pipe(tap(r => this.setAuth(r)));
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this._currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null { return localStorage.getItem(this.TOKEN_KEY); }

  refreshCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>('/api/users/me').pipe(
      tap(u => { this._currentUser.set(u); localStorage.setItem(this.USER_KEY, JSON.stringify(u)); })
    );
  }

  private setAuth(res: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(res.user));
    this._currentUser.set(res.user);
  }

  private loadUser(): UserDTO | null {
    try { return JSON.parse(localStorage.getItem(this.USER_KEY) ?? 'null'); }
    catch { return null; }
  }
}
