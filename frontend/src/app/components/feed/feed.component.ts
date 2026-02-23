import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PostCardComponent } from '../post-card/post-card.component';
import { PostService } from '../../services/post.service';
import { AuthService } from '../../services/auth.service';
import { PostDTO } from '../../models';
import { pick, FEED_QUOTES, EMPTY_FEED_MSGS } from '../../services/typewriter.service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, RouterLink, PostCardComponent],
  template: `
    <div class="narrow animate-in">

      <div class="feed-head">
        <div class="feed-meta text-mono text-dim text-xs">
          <span>■ {{ username }}</span>
          <span class="sep">|</span>
          <span>{{ today }}</span>
          <span class="sep">|</span>
          <span>{{ posts.length }} TX</span>
        </div>
        <div class="flex justify-between items-center">
          <h1 class="section-title">INCOMING FEED</h1>
          <button routerLink="/posts/new" class="btn btn-primary btn-sm">TRANSMIT</button>
        </div>
        @if (quote) { <p class="flavor-quote">{{ quote }}</p> }
      </div>

      @if (loading) {
        <div class="loading-wrap">TUNING TO YOUR FREQUENCY<span class="cursor"></span></div>
      }

      @if (!loading && posts.length === 0) {
        <div class="empty-state">
          <p class="empty-msg">{{ emptyMsg }}</p>
          <p class="text-dim text-mono text-sm mb-3">Follow users from explore to populate your feed.</p>
          <button routerLink="/explore" class="btn btn-primary">SCAN FOR SIGNALS</button>
        </div>
      }

      <div class="stagger">
        @for (post of posts; track post.id) {
          <app-post-card [post]="post" (deleted)="remove($event)" (likeToggled)="update($event)" />
        }
      </div>

    </div>
  `,
  styles: [`
    .feed-head { margin-bottom: 1.2rem; }
    .feed-meta { display: flex; gap: 0.6rem; flex-wrap: wrap; margin-bottom: 0.4rem; }
    .sep       { opacity: 0.3; }
  `]
})
export class FeedComponent implements OnInit {
  private postSvc = inject(PostService);
  private auth    = inject(AuthService);

  posts: PostDTO[] = [];
  loading  = true;
  username = this.auth.currentUser()?.username ?? '';
  quote    = '';
  emptyMsg = '';
  today    = new Date().toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' }).toUpperCase();

  ngOnInit() {
    this.quote    = pick(FEED_QUOTES);
    this.emptyMsg = pick(EMPTY_FEED_MSGS);
    this.postSvc.getFeed().subscribe({
      next:  p => { this.posts = p; this.loading = false; },
      error: () => this.loading = false
    });
  }

  remove(id: number) { this.posts = this.posts.filter(p => p.id !== id); }
  update(p: PostDTO) { this.posts = this.posts.map(x => x.id === p.id ? p : x); }
}
