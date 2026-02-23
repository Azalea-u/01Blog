import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class PostService {
  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<PostDTO[]>            { return this.http.get<PostDTO[]>('/api/posts'); }
  getFeed(): Observable<PostDTO[]>               { return this.http.get<PostDTO[]>('/api/posts/feed'); }
  getPostById(id: number): Observable<PostDTO>   { return this.http.get<PostDTO>(`/api/posts/${id}`); }
  getPostsByUser(uid: number): Observable<PostDTO[]> { return this.http.get<PostDTO[]>(`/api/posts/user/${uid}`); }

  createPost(title: string, content: string, media?: File): Observable<PostDTO> {
    const form = new FormData();
    form.append('title', title);
    form.append('content', content);
    if (media) form.append('media', media);
    return this.http.post<PostDTO>('/api/posts', form);
  }

  updatePost(id: number, title: string, content: string, media?: File): Observable<PostDTO> {
    const form = new FormData();
    form.append('title', title);
    form.append('content', content);
    if (media) form.append('media', media);
    return this.http.put<PostDTO>(`/api/posts/${id}`, form);
  }

  deletePost(id: number): Observable<void>  { return this.http.delete<void>(`/api/posts/${id}`); }
  adminDeletePost(id: number): Observable<void> { return this.http.delete<void>(`/api/posts/${id}/admin`); }

  likePost(id: number): Observable<unknown>   { return this.http.post(`/api/posts/${id}/likes`, {}); }
  unlikePost(id: number): Observable<unknown> { return this.http.delete(`/api/posts/${id}/likes`); }
  getLikeStatus(id: number): Observable<{ liked: boolean }> {
    return this.http.get<{ liked: boolean }>(`/api/posts/${id}/likes/status`);
  }
}
