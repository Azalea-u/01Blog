import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostService } from '../../services/post.service';
import { MediaPlayerComponent } from '../media-player/media-player.component';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, MediaPlayerComponent],
  template: `
    <div class="narrow animate-in">

      <div class="page-head">
        <div class="text-mono text-dim text-xs mb-1">
          {{ isEdit ? 'MODIFY TX #' + postId : 'COMPOSE NEW TRANSMISSION' }}
        </div>
        <h1 class="section-title">{{ isEdit ? 'EDIT POST' : 'NEW POST' }}</h1>
        <p class="flavor-quote">
          {{ isEdit ? '"Revise. Refine. Retransmit."' : '"Every signal starts as silence."' }}
        </p>
      </div>

      @if (error)       { <div class="alert alert-error">! {{ error }}</div> }
      @if (loadingPost) { <div class="loading-wrap">FETCHING RECORD<span class="cursor"></span></div> }

      @if (!loadingPost) {
        <form [formGroup]="form" (ngSubmit)="submit()">

          <div class="form-group">
            <label class="field-label">TITLE</label>
            <input type="text" formControlName="title" placeholder="post title..." maxlength="200" />
            <div class="field-foot">
              @if (f['title'].invalid && f['title'].touched) {
                <span class="form-error">MIN 3 CHARS REQUIRED</span>
              } @else { <span></span> }
              <span class="char-count">{{ f['title'].value?.length ?? 0 }}/200</span>
            </div>
          </div>

          <div class="form-group">
            <label class="field-label">CONTENT</label>
            <textarea formControlName="content" placeholder="write your post..." rows="16" maxlength="10000"></textarea>
            <div class="field-foot">
              @if (f['content'].invalid && f['content'].touched) {
                <span class="form-error">CONTENT REQUIRED</span>
              } @else { <span></span> }
              <span class="char-count">{{ f['content'].value?.length ?? 0 }}/10000</span>
            </div>
          </div>

          <!-- Media section -->
          <div class="form-group">
            <label class="field-label">MEDIA <span class="label-opt">— image or video, optional</span></label>

            <!-- Existing media (edit mode, no new file chosen) -->
            @if (existingMediaUrl && existingMediaType && !mediaFile) {
              <div class="existing-media">
                <div class="existing-label text-mono text-dim text-xs">CURRENT ATTACHMENT</div>
                <app-media-player [mediaUrl]="existingMediaUrl" [mediaType]="existingMediaType" alt="current media" />
                <button type="button" class="btn btn-xs btn-danger" (click)="removeExisting()">REMOVE ATTACHMENT</button>
              </div>
            }

            <!-- New file preview using MediaPlayerComponent -->
            @if (previewUrl && previewType) {
              <div class="new-media">
                <div class="existing-label text-mono text-dim text-xs">NEW ATTACHMENT</div>
                <app-media-player [mediaUrl]="previewUrl" [mediaType]="previewType" alt="preview" />
                <button type="button" class="btn btn-xs btn-danger mt-1" (click)="clearMedia()">REMOVE</button>
              </div>
            }

            <!-- Upload zone — hide when new file chosen -->
            @if (!previewUrl) {
              <div class="upload-zone" [class.has-file]="!!existingMediaUrl && !removeMedia"
                (click)="fileInput.click()">
                @if (existingMediaUrl && !removeMedia) {
                  <span class="upload-line">+ REPLACE ATTACHMENT</span>
                } @else {
                  <span class="upload-line">+ ATTACH FILE</span>
                }
                <span class="upload-sub">image &middot; video</span>
              </div>
            } @else {
              <!-- Replace button when new file is already picked -->
              <div class="upload-replace" (click)="fileInput.click()">
                <span class="text-mono text-dim text-xs">+ REPLACE WITH DIFFERENT FILE</span>
              </div>
            }

            <input #fileInput type="file" accept="image/*,video/*"
              (change)="onFile($event)" style="display:none" />
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary" [disabled]="loading || form.invalid">
              @if (loading) {
                {{ isEdit ? 'SAVING' : 'PUBLISHING' }}<span class="cursor"></span>
              } @else {
                {{ isEdit ? 'SAVE CHANGES' : 'PUBLISH POST' }}
              }
            </button>
            <button routerLink="/explore" class="btn">CANCEL</button>
          </div>

        </form>
      }
    </div>
  `,
  styles: [`
    .page-head  { margin-bottom: 1.4rem; }
    .label-opt  { font-size: 0.72rem; opacity: 0.5; text-transform: none; letter-spacing: 0; font-family: var(--font-mono); }
    .field-foot { display: flex; justify-content: space-between; align-items: center; margin-top: 0.25rem; }
    .char-count { font-family: var(--font-mono); font-size: 0.68rem; color: var(--C-dim); opacity: 0.5; }

    .existing-media, .new-media {
      margin-bottom: 0.6rem;
    }
    .existing-label {
      margin-bottom: 0.4rem;
      letter-spacing: 0.08em;
      opacity: 0.5;
    }

    .upload-zone {
      display: flex; flex-direction: column; align-items: center; gap: 0.3rem;
      padding: 1.6rem; border-bottom: 1px solid var(--border-dim);
      cursor: pointer; text-align: center;
      transition: border-color var(--t-mid), background var(--t-mid);
      &:hover    { border-color: var(--border-mid); background: rgba(91,248,112,0.025); }
      &.has-file { border-color: var(--C-dim); background: rgba(91,248,112,0.03); }
    }
    .upload-line { font-family: var(--font-main); font-size: 1rem; color: var(--C-dim); letter-spacing: 0.06em; }
    .upload-sub  { font-family: var(--font-mono); font-size: 0.7rem; color: var(--C-dim); opacity: 0.45; }

    .upload-replace {
      padding: 0.5rem 0;
      cursor: pointer;
      opacity: 0.5;
      transition: opacity var(--t-fast);
      &:hover { opacity: 1; }
    }

    .form-actions { display: flex; align-items: center; gap: 0.8rem; margin-top: 1.4rem; }
  `]
})
export class CreatePostComponent implements OnInit, OnDestroy {
  private fb      = inject(FormBuilder);
  private postSvc = inject(PostService);
  private router  = inject(Router);
  private route   = inject(ActivatedRoute);

  form = this.fb.group({
    title:   ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
    content: ['', [Validators.required, Validators.maxLength(10000)]]
  });
  get f() { return this.form.controls; }

  isEdit      = false;
  postId:     number | null = null;
  loadingPost = false;
  loading     = false;
  error       = '';
  removeMedia = false;

  // Existing media from server (edit mode)
  existingMediaUrl:  string | null = null;
  existingMediaType: 'IMAGE' | 'VIDEO' | null = null;

  // New file chosen by user
  mediaFile:   File | null = null;
  previewUrl:  string | null = null;
  previewType: 'IMAGE' | 'VIDEO' | null = null;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;
    this.isEdit      = true;
    this.postId      = Number(id);
    this.loadingPost = true;
    this.postSvc.getPostById(this.postId).subscribe({
      next: p => {
        this.form.patchValue({ title: p.title, content: p.content });
        // Populate existing media so it's shown in edit mode
        if (p.mediaUrl && p.mediaType && (p.mediaType === 'IMAGE' || p.mediaType === 'VIDEO')) {
          this.existingMediaUrl  = p.mediaUrl;
          this.existingMediaType = p.mediaType;
        }
        this.loadingPost = false;
      },
      error: () => { this.error = 'FAILED TO LOAD POST.'; this.loadingPost = false; }
    });
  }

  onFile(e: Event) {
    const file = (e.target as HTMLInputElement).files?.[0];
    if (!file) return;
    if (this.previewUrl) URL.revokeObjectURL(this.previewUrl);
    this.mediaFile  = file;
    this.previewUrl = URL.createObjectURL(file);
    if      (file.type.startsWith('image/')) this.previewType = 'IMAGE';
    else if (file.type.startsWith('video/')) this.previewType = 'VIDEO';
    else { this.previewUrl = null; this.previewType = null; this.mediaFile = null; }
  }

  clearMedia() {
    if (this.previewUrl) URL.revokeObjectURL(this.previewUrl);
    this.mediaFile = null; this.previewUrl = null; this.previewType = null;
  }

  removeExisting() {
    this.existingMediaUrl = null; this.existingMediaType = null; this.removeMedia = true;
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true; this.error = '';
    const { title, content } = this.form.value;
    const action = this.isEdit && this.postId
      ? this.postSvc.updatePost(this.postId, title!, content!, this.mediaFile ?? undefined)
      : this.postSvc.createPost(title!, content!, this.mediaFile ?? undefined);
    action.subscribe({
      next:  p  => this.router.navigate(['/posts', p.id]),
      error: e  => { this.error = e.error?.message ?? 'FAILED TO PUBLISH.'; this.loading = false; }
    });
  }

  ngOnDestroy() { if (this.previewUrl) URL.revokeObjectURL(this.previewUrl); }

  fmtSize(b: number): string {
    if (b < 1024)    return `${b} B`;
    if (b < 1048576) return `${(b / 1024).toFixed(1)} KB`;
    return `${(b / 1048576).toFixed(1)} MB`;
  }
}
