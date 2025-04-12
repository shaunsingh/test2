import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorAnalyzerService, ErrorData } from '../../services/error-analyzer.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faSearch, faFilter } from '@fortawesome/free-solid-svg-icons';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, FormsModule],
  template: `
    <div class="accounts-container">
      <h1>Error Accounts</h1>
      
      <div class="search-bar">
        <div class="search-input">
          <fa-icon [icon]="faSearch"></fa-icon>
          <input type="text" placeholder="Search errors..." [(ngModel)]="searchTerm" (ngModelChange)="filterErrors()">
        </div>
        <div class="filter-dropdown">
          <fa-icon [icon]="faFilter"></fa-icon>
          <select [(ngModel)]="selectedType" (ngModelChange)="filterErrors()">
            <option value="">All Types</option>
            <option *ngFor="let type of errorTypes" [value]="type">{{ type }}</option>
          </select>
        </div>
      </div>

      <div class="error-list">
        <div class="error-item" *ngFor="let error of filteredErrors">
          <div class="error-header">
            <span class="error-type">{{ error.errorType }}</span>
            <span class="error-time">{{ error.timestamp | date:'medium' }}</span>
          </div>
          <p class="error-message">{{ error.message }}</p>
          <div class="error-details" *ngIf="error.metadata">
            <div class="metadata-item" *ngFor="let key of getMetadataKeys(error.metadata)">
              <span class="metadata-key">{{ key }}:</span>
              <span class="metadata-value">{{ error.metadata[key] }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .accounts-container {
      padding: 20px;
    }

    .search-bar {
      display: flex;
      gap: 15px;
      margin: 20px 0;
    }

    .search-input, .filter-dropdown {
      display: flex;
      align-items: center;
      background: white;
      padding: 10px;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .search-input input {
      border: none;
      outline: none;
      margin-left: 10px;
      width: 200px;
    }

    .filter-dropdown select {
      border: none;
      outline: none;
      margin-left: 10px;
      background: transparent;
    }

    .error-list {
      margin-top: 20px;
    }

    .error-item {
      background: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;
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
      margin: 10px 0;
    }

    .error-details {
      margin-top: 10px;
      padding-top: 10px;
      border-top: 1px solid #eee;
    }

    .metadata-item {
      display: flex;
      gap: 10px;
      margin-bottom: 5px;
    }

    .metadata-key {
      font-weight: bold;
      color: #666;
    }

    .metadata-value {
      color: #333;
    }
  `]
})
export class AccountsComponent implements OnInit {
  errors: ErrorData[] = [];
  filteredErrors: ErrorData[] = [];
  errorTypes: string[] = [];
  searchTerm: string = '';
  selectedType: string = '';

  faSearch = faSearch;
  faFilter = faFilter;

  constructor(private errorService: ErrorAnalyzerService) {}

  ngOnInit() {
    this.errorService.getErrors().subscribe(
      errors => {
        this.errors = errors;
        this.filteredErrors = errors;
        this.errorTypes = [...new Set(errors.map(e => e.errorType))];
      },
      error => console.error('Error fetching errors:', error)
    );
  }

  filterErrors() {
    this.filteredErrors = this.errors.filter(error => {
      const matchesSearch = error.message.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                          error.errorType.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesType = !this.selectedType || error.errorType === this.selectedType;
      return matchesSearch && matchesType;
    });
  }

  getMetadataKeys(metadata: any): string[] {
    return Object.keys(metadata);
  }
} 