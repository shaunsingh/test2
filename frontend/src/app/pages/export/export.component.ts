import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorAnalyzerService } from '../../services/error-analyzer.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faFileCsv, faFileCode } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-export',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  template: `
    <div class="export-container">
      <h1>Export</h1>
      
      <div class="export-options">
        <div class="export-card">
          <fa-icon [icon]="faFileCsv" class="export-icon"></fa-icon>
          <h3>Export as CSV</h3>
          <p>Download error data in CSV format</p>
          <button (click)="exportData('csv')" class="export-button">Export CSV</button>
        </div>

        <div class="export-card">
          <fa-icon [icon]="faFileCode" class="export-icon"></fa-icon>
          <h3>Export as JSON</h3>
          <p>Download error data in JSON format</p>
          <button (click)="exportData('json')" class="export-button">Export JSON</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .export-container {
      padding: 20px;
    }

    .export-options {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }

    .export-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      text-align: center;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .export-icon {
      font-size: 2.5rem;
      color: #1a1f36;
      margin-bottom: 15px;
    }

    .export-card h3 {
      margin: 10px 0;
      color: #1a1f36;
    }

    .export-card p {
      color: #666;
      margin-bottom: 20px;
    }

    .export-button {
      background-color: #1a1f36;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: #2a2f46;
      }
    }
  `]
})
export class ExportComponent {
  faFileCsv = faFileCsv;
  faFileCode = faFileCode;

  constructor(private errorService: ErrorAnalyzerService) {}

  exportData(format: 'csv' | 'json') {
    this.errorService.exportErrors(format).subscribe(
      (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `errors.${format}`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error => console.error('Error exporting data:', error)
    );
  }
} 