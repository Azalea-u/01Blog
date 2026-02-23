import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PostCardComponent } from '../post-card/post-card.component';
import { PostService } from '../../services/post.service';
import { PostDTO } from '../../models';
import { pick, EXPLORE_QUOTES } from '../../services/typewriter.service';

@Component({
  selector: 'app-explore',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, PostCardComponent],
  template: `
    <div class="narrow animate-in">

      <div class="explore-head">
        <div class="text-mono text-dim text-xs mb-1">PUBLIC BROADCAST — ALL NODES</div>
        <div class="flex justify-between items-center">
          <h1 class="section-title">EXPLORE</h1>
          <button routerLink="/posts/new" class="btn btn-primary btn-sm">TRANSMIT</button>
        </div>
        @if (statusLine) { <p class="flavor-quote">{{ statusLine }}</p> }
      </div>

      <div class="search-row">
        <span class="search-prompt text-mono text-dim">$</span>
        <input type="search" [(ngModel)]="q" (ngModelChange)="filter()"
          placeholder="search title, author, content..." />
      </div>

      @if (loading) {
        <div class="loading-wrap">{{ scanMsg }}<span class="cursor"></span></div>
      }

      @if (!loading) {
        <div class="result-bar text-mono text-dim text-xs mb-2">
          @if (q) {
            QUERY "{{ q }}" — {{ filtered.length }} RESULT{{ filtered.length !== 1 ? 'S' : '' }} / {{ all.length }} TOTAL
          } @else {
            {{ all.length }} TRANSMISSION{{ all.length !== 1 ? 'S' : '' }} ON FILE
          }
        </div>

        @if (filtered.length === 0 && q) {
          <div class="empty-state">
            <p class="empty-msg">NO SIGNAL MATCHING "{{ q }}"</p>
          </div>
        }

        <div class="stagger">
          @for (post of filtered; track post.id) {
            <app-post-card [post]="post" (deleted)="remove($event)" (likeToggled)="update($event)" />
          }
        </div>
      }

    </div>
  `,
  styles: [`
    .explore-head { margin-bottom: 1rem; }
    .search-row {
      display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.7rem;
      .search-prompt { flex-shrink: 0; font-size: 0.9rem; }
    }
  `]
})
export class ExploreComponent implements OnInit {
  private postSvc = inject(PostService);

  all:       PostDTO[] = [];
  filtered:  PostDTO[] = [];
  q          = '';
  loading    = true;
  statusLine = '';
  scanMsg    = '';

  ngOnInit() {
    this.statusLine = pick(EXPLORE_QUOTES);
    this.scanMsg    = pick(['INTERCEPTING BROADCASTS', 'QUERYING ALL NODES', 'PARSING THE DATASTREAM', 'SCANNING ARCHIVE']);
    this.postSvc.getAllPosts().subscribe({
      next:  p => { this.all = p; this.filtered = p; this.loading = false; },
      error: () => this.loading = false
    });
  }

  filter() {
    const lq = this.q.toLowerCase().trim();
    this.filtered = lq
      ? this.all.filter(p =>
          p.title.toLowerCase().includes(lq) ||
          p.username.toLowerCase().includes(lq) ||
          p.content.toLowerCase().includes(lq))
      : [...this.all];
  }

  remove(id: number) { this.all = this.all.filter(p => p.id !== id); this.filter(); }
  update(p: PostDTO)  { this.all = this.all.map(x => x.id === p.id ? p : x); this.filter(); }
}
