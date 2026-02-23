import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { TypewriterService, TypewriterLine, BOOT_LINES } from '../../services/typewriter.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  template: `
    <div class="auth-page animate-in">
      <div class="auth-shell">

        <!-- Terminal chrome bar -->
        <div class="auth-chrome">
          <span class="chrome-dot"></span>
          <span class="chrome-dot"></span>
          <span class="chrome-dot"></span>
          <span class="chrome-title">TERMINAL::AUTH — v4.1.0</span>
        </div>

        <div class="auth-body">
          <!-- Boot sequence -->
          <div class="boot-seq">
            @for (line of bootLines; track $index) {
              <div class="boot-line">
                <span class="boot-text">{{ line.text }}</span>
                @if (line.done && line.status) {
                  <span class="boot-status" [class]="line.status">[ {{ line.status.toUpperCase() }} ]</span>
                }
                @if (!line.done) { <span class="cursor"></span> }
              </div>
            }
          </div>

          <h1 class="auth-heading">\\ IDENTIFY YOURSELF</h1>
          @if (quote) { <p class="flavor-quote">{{ quote }}</p> }

          @if (error) { <div class="alert alert-error">! {{ error }}</div> }

          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="form-group">
              <label class="field-label">HANDLE</label>
              <input type="text" formControlName="username"
                placeholder="username" autocomplete="username" />
              @if (f['username'].invalid && f['username'].touched) {
                <div class="form-error">HANDLE REQUIRED</div>
              }
            </div>

            <div class="form-group">
              <label class="field-label">PASSPHRASE</label>
              <input type="password" formControlName="password"
                placeholder="••••••••" autocomplete="current-password" />
              @if (f['password'].invalid && f['password'].touched) {
                <div class="form-error">PASSPHRASE REQUIRED</div>
              }
            </div>

            <button type="submit" class="btn btn-primary auth-submit"
              [disabled]="loading || form.invalid || !bootDone">
              @if (loading) { AUTHENTICATING<span class="cursor"></span> }
              @else { ENTER NETWORK }
            </button>
          </form>

          <div class="auth-switch">
            <span>NOT REGISTERED?</span>
            <a routerLink="/register">REQUEST ACCESS</a>
          </div>
        </div>

      </div>
    </div>
  `,
  styles: [`
    .auth-page {
      display: flex;
      align-items: flex-start;
      justify-content: center;
      padding-top: 3.5rem;
    }

    .auth-shell {
      width: 100%;
      max-width: 420px;
      border: 1px solid var(--border-dim);
      background: var(--Bg-card);
      box-shadow: 0 0 60px -12px rgba(91,248,112,0.15), inset 0 1px 0 var(--border-dim);
      animation: modal-drop 0.28s var(--ease) both;
    }

    /* Terminal window chrome */
    .auth-chrome {
      display: flex;
      align-items: center;
      gap: 0.45em;
      padding: 0.55rem 0.9rem;
      border-bottom: 1px solid var(--border-dim);
      background: rgba(91,248,112,0.025);
    }

    .chrome-dot {
      width: 7px; height: 7px;
      border: 1px solid var(--border-mid);
      display: inline-block;
      flex-shrink: 0;
    }

    .chrome-title {
      font-family: var(--font-mono);
      font-size: 0.62rem;
      color: var(--C-dim);
      opacity: 0.45;
      letter-spacing: 0.08em;
      margin-left: auto;
    }

    .auth-body {
      padding: 1.8rem;
    }

    .auth-heading {
      font-size: 1.6rem;
      letter-spacing: 0.1em;
      margin-bottom: 0.5rem;
      text-shadow: 0 0 14px var(--Ts-faint);
    }

    .auth-submit {
      width: 100%;
      justify-content: center;
      font-size: 1.05rem;
      margin-top: 0.5rem;
      padding: 0.25em 0;
    }

    .auth-switch {
      margin-top: 1.3rem;
      padding-top: 1rem;
      border-top: 1px solid var(--border-dim);
      display: flex;
      gap: 0.7rem;
      align-items: center;
      font-family: var(--font-mono);
      font-size: 0.72rem;
      color: var(--C-dim);
      opacity: 0.6;
      a { font-size: 0.72rem; opacity: 1; }
    }
  `]
})
export class LoginComponent implements OnInit {
  private fb     = inject(FormBuilder);
  private auth   = inject(AuthService);
  private router = inject(Router);
  private tw     = inject(TypewriterService);

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
  get f() { return this.form.controls; }

  loading  = false;
  bootDone = false;
  error    = '';
  quote    = '';
  bootLines: TypewriterLine[] = [];

  private quotes = [
    '"The terminal never lies. People do."',
    '"Signal strong. Noise minimal. Proceed."',
    '"Broadcast into the void. Someone is listening."',
    '"All systems nominal. Your move, operator."',
  ];

  async ngOnInit() {
    const n = 3 + Math.floor(Math.random() * 3);
    await this.tw.runSequence(BOOT_LINES.slice(0, n), this.bootLines, 16, 70);
    this.bootDone = true;
    this.quote = this.quotes[Math.floor(Math.random() * this.quotes.length)];
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true; this.error = '';
    const { username, password } = this.form.value;
    this.auth.login({ username: username!, password: password! }).subscribe({
      next:  () => this.router.navigate(['/feed']),
      error: e  => { this.error = e.error?.error ?? 'ACCESS DENIED.'; this.loading = false; }
    });
  }
}
