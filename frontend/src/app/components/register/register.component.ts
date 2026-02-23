import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { TypewriterService, TypewriterLine, REGISTER_BOOT } from '../../services/typewriter.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  template: `
    <div class="auth-page animate-in">
      <div class="auth-shell">

        <div class="auth-chrome">
          <span class="chrome-dot"></span>
          <span class="chrome-dot"></span>
          <span class="chrome-dot"></span>
          <span class="chrome-title">TERMINAL::REGISTER — NODE ALLOCATION</span>
        </div>

        <div class="auth-body">
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

          <h1 class="auth-heading">\\ JOIN THE NETWORK</h1>
          <p class="flavor-quote">"Every operator starts here. The rest is up to you."</p>

          @if (error) { <div class="alert alert-error">! {{ error }}</div> }

          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="form-group">
              <label class="field-label">HANDLE</label>
              <input type="text" formControlName="username"
                placeholder="min 3 characters" autocomplete="username" />
              @if (f['username'].invalid && f['username'].touched) {
                <div class="form-error">MIN 3 CHARACTERS REQUIRED</div>
              }
            </div>
            <div class="form-group">
              <label class="field-label">COMM CHANNEL</label>
              <input type="email" formControlName="email"
                placeholder="your@email.com" autocomplete="email" />
              @if (f['email'].invalid && f['email'].touched) {
                <div class="form-error">VALID EMAIL REQUIRED</div>
              }
            </div>
            <div class="form-group">
              <label class="field-label">PASSPHRASE</label>
              <input type="password" formControlName="password"
                placeholder="min 6 characters" autocomplete="new-password" />
              @if (f['password'].invalid && f['password'].touched) {
                <div class="form-error">MIN 6 CHARACTERS REQUIRED</div>
              }
            </div>
            <div class="form-group">
              <label class="field-label">CONFIRM PASSPHRASE</label>
              <input type="password" formControlName="confirmPassword"
                placeholder="repeat passphrase" autocomplete="new-password" />
              @if (form.errors?.['mismatch'] && f['confirmPassword'].touched) {
                <div class="form-error">PASSPHRASES DO NOT MATCH</div>
              }
            </div>

            <button type="submit" class="btn btn-primary auth-submit" [disabled]="loading || form.invalid">
              @if (loading) { REGISTERING<span class="cursor"></span> }
              @else { INITIALIZE ACCOUNT }
            </button>
          </form>

          <div class="auth-switch">
            <span>ALREADY REGISTERED?</span>
            <a routerLink="/login">AUTHENTICATE</a>
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
      padding-top: 2.5rem;
    }

    .auth-shell {
      width: 100%;
      max-width: 420px;
      border: 1px solid var(--border-dim);
      background: var(--Bg-card);
      box-shadow: 0 0 60px -12px rgba(91,248,112,0.15), inset 0 1px 0 var(--border-dim);
      animation: modal-drop 0.28s var(--ease) both;
    }

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
    }

    .chrome-title {
      font-family: var(--font-mono);
      font-size: 0.62rem;
      color: var(--C-dim);
      opacity: 0.45;
      letter-spacing: 0.08em;
      margin-left: auto;
    }

    .auth-body    { padding: 1.8rem; }
    .auth-heading { font-size: 1.6rem; letter-spacing: 0.1em; margin-bottom: 0.5rem; text-shadow: 0 0 14px var(--Ts-faint); }
    .auth-submit  { width: 100%; justify-content: center; font-size: 1.05rem; margin-top: 0.5rem; padding: 0.25em 0; }

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
export class RegisterComponent implements OnInit {
  private fb     = inject(FormBuilder);
  private auth   = inject(AuthService);
  private router = inject(Router);
  private tw     = inject(TypewriterService);

  form = this.fb.group({
    username:        ['', [Validators.required, Validators.minLength(3)]],
    email:           ['', [Validators.required, Validators.email]],
    password:        ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required]
  }, {
    validators: (c: AbstractControl) =>
      c.get('password')?.value === c.get('confirmPassword')?.value ? null : { mismatch: true }
  });
  get f() { return this.form.controls; }

  loading   = false;
  error     = '';
  bootLines: TypewriterLine[] = [];

  async ngOnInit() {
    await this.tw.runSequence(REGISTER_BOOT, this.bootLines, 14, 65);
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true; this.error = '';
    const { username, email, password } = this.form.value;
    this.auth.register({ username: username!, email: email!, password: password! }).subscribe({
      next:  () => this.router.navigate(['/feed']),
      error: e  => { this.error = e.error?.message ?? 'REGISTRATION FAILED.'; this.loading = false; }
    });
  }
}
