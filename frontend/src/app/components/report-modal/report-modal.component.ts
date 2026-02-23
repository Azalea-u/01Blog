import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-report-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="modal-overlay" (click)="onOverlayClick($event)">
      <div class="modal-box animate-in" role="dialog" aria-modal="true">

        <h2 class="modal-title">REPORT: {{ targetUsername }}</h2>

        @if (postId) {
          <p class="text-dim text-mono text-sm mb-2">■ {{ targetUsername }} — post #{{ postId }}</p>
        }

        <div class="form-group">
          <label class="field-label">REASON FOR REPORT</label>
          <textarea [(ngModel)]="reason" placeholder="describe the violation clearly..."
            rows="4" maxlength="500"></textarea>
          <div class="text-right text-xs text-dim mt-1 text-mono">{{ reason.length }}/500</div>
        </div>

        @if (error)   { <div class="alert alert-error">! {{ error }}</div> }
        @if (success) { <div class="alert alert-success">&#10003; {{ success }}</div> }

        <div class="modal-actions">
          <button class="btn" (click)="close.emit()" [disabled]="loading">CANCEL</button>
          <button class="btn btn-danger" (click)="submit()"
            [disabled]="loading || !reason.trim() || !!success">
            @if (loading) { SUBMITTING<span class="cursor"></span> }
            @else         { SUBMIT REPORT }
          </button>
        </div>

      </div>
    </div>
  `
})
export class ReportModalComponent {
  @Input() targetUserId!:  number;
  @Input() targetUsername!: string;
  @Input() postId?:         number;
  @Output() close     = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<void>();

  private reportSvc = inject(ReportService);

  reason  = '';
  loading = false;
  error   = '';
  success = '';

  onOverlayClick(e: MouseEvent) {
    if ((e.target as HTMLElement).classList.contains('modal-overlay')) this.close.emit();
  }

  submit() {
    if (!this.reason.trim()) return;
    this.loading = true; this.error = '';
    if (this.reason.length > 500) {
      this.error = 'REPORT TOO LONG.';
      this.loading = false;
      return;
    } else if (this.reason.length < 10) {
      this.error = 'REPORT TOO SHORT.';
      this.loading = false;
      return;
    }

    this.reportSvc.createReport(this.targetUserId, this.reason.trim(), this.postId).subscribe({
      next: () => {
        this.success = 'REPORT SUBMITTED. ADMINS WILL REVIEW.';
        this.loading = false;
        setTimeout(() => { this.submitted.emit(); this.close.emit(); }, 1800);
      },
      error: e => { this.error = e.error?.message ?? 'FAILED TO SUBMIT.'; this.loading = false; }
    });
  }
}
