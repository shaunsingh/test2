import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorAnalyzerService, ErrorStats } from '../../services/error-analyzer.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faExclamationTriangle, faChartBar, faClock } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  template: `
    <div class="overview-container">
      <h1>Overview</h1>
      
      <div class="stats-grid" *ngIf="stats">
        <div class="stat-card">
          <fa-icon [icon]="faExclamationTriangle" class="stat-icon"></fa-icon>
          <div class="stat-content">
            <h3>Total Errors</h3>
            <p class="stat-value">{{ stats.totalErrors }}</p>
          </div>
        </div>

        <div class="stat-card">
          <fa-icon [icon]="faChartBar" class="stat-icon"></fa-icon>
          <div class="stat-content">
            <h3>Error Types</h3>
            <p class="stat-value">{{ getErrorTypesCount() }}</p>
          </div>
        </div>

        <div class="stat-card">
          <fa-icon [icon]="faClock" class="stat-icon"></fa-icon>
          <div class="stat-content">
            <h3>Recent Errors</h3>
            <p class="stat-value">{{ stats.recentErrors.length }}</p>
          </div>
        </div>
      </div>

      <div class="recent-errors" *ngIf="stats">
        <h2>Recent Errors</h2>
        <div class="error-list">
          <div class="error-item" *ngFor="let error of stats.recentErrors">
            <div class="error-header">
              <span class="error-type">{{ error.errorType }}</span>
              <span class="error-time">{{ error.timestamp | date:'medium' }}</span>
            </div>
            <p class="error-message">{{ error.message }}</p>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .overview-container {
      padding: 20px;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin: 20px 0;
    }

    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      display: flex;
      align-items: center;
      gap: 15px;
    }

    .stat-icon {
      font-size: 2rem;
      color: #1a1f36;
    }

    .stat-content {
      flex: 1;
    }

    .stat-value {
      font-size: 1.5rem;
      font-weight: bold;
      margin: 5px 0 0;
    }

    .recent-errors {
      margin-top: 30px;
    }

    .error-list {
      margin-top: 15px;
    }

    .error-item {
      background: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 10px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .error-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
    }

    .error-type {
      font-weight: bold;
      color: #1a1f36;
    }

    .error-time {
      color: #666;
      font-size: 0.9rem;
    }

    .error-message {
      color: #333;
      margin: 0;
    }
  `]
})
export class OverviewComponent implements OnInit {
  stats: ErrorStats | null = null;
  faExclamationTriangle = faExclamationTriangle;
  faChartBar = faChartBar;
  faClock = faClock;

  constructor(private errorService: ErrorAnalyzerService) {}

  ngOnInit() {
    this.errorService.getErrorStats().subscribe(
      stats => this.stats = stats,
      error => console.error('Error fetching stats:', error)
    );
  }

  getErrorTypesCount(): number {
    return this.stats ? Object.keys(this.stats.errorTypes).length : 0;
  }
} 