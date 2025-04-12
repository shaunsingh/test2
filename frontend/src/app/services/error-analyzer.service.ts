import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ErrorData {
  id: string;
  timestamp: string;
  errorType: string;
  message: string;
  stackTrace: string;
  metadata: {
    [key: string]: any;
  };
}

export interface ErrorStats {
  totalErrors: number;
  errorTypes: {
    [key: string]: number;
  };
  recentErrors: ErrorData[];
}

@Injectable({
  providedIn: 'root'
})
export class ErrorAnalyzerService {
  private apiUrl = 'http://localhost:3000/api';

  constructor(private http: HttpClient) {}

  getErrorStats(): Observable<ErrorStats> {
    return this.http.get<ErrorStats>(`${this.apiUrl}/stats`);
  }

  getErrors(page: number = 1, limit: number = 10): Observable<ErrorData[]> {
    return this.http.get<ErrorData[]>(`${this.apiUrl}/errors?page=${page}&limit=${limit}`);
  }

  getErrorById(id: string): Observable<ErrorData> {
    return this.http.get<ErrorData>(`${this.apiUrl}/errors/${id}`);
  }

  exportErrors(format: 'csv' | 'json' = 'json'): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export?format=${format}`, {
      responseType: 'blob'
    });
  }
} 