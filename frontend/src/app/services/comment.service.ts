import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class CommentService {
  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`/api/posts/${postId}/comments`);
  }
  addComment(postId: number, content: string): Observable<CommentDTO> {
    return this.http.post<CommentDTO>(`/api/posts/${postId}/comments`, { content });
  }
  updateComment(id: number, content: string): Observable<CommentDTO> {
    return this.http.put<CommentDTO>(`/api/comments/${id}`, { content });
  }
  deleteComment(id: number): Observable<void> {
    return this.http.delete<void>(`/api/comments/${id}`);
  }
}
