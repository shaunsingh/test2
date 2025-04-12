import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faChartLine, faUser, faFileExport } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FontAwesomeModule],
  template: `
    <div class="sidebar">
      <div class="logo">
        <h1>Error Analyzer</h1>
      </div>
      <nav class="nav-menu">
        <a routerLink="/overview" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
          <fa-icon [icon]="faChartLine"></fa-icon>
          <span>Overview</span>
        </a>
        <a routerLink="/accounts" routerLinkActive="active">
          <fa-icon [icon]="faUser"></fa-icon>
          <span>Accounts</span>
        </a>
        <a routerLink="/export" routerLinkActive="active">
          <fa-icon [icon]="faFileExport"></fa-icon>
          <span>Export</span>
        </a>
      </nav>
    </div>
  `,
  styles: [`
    .sidebar {
      width: 250px;
      height: 100vh;
      background-color: #1a1f36;
      color: white;
      padding: 1rem;
    }

    .logo {
      padding: 1rem;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      
      h1 {
        margin: 0;
        font-size: 1.5rem;
        font-weight: 500;
      }
    }

    .nav-menu {
      margin-top: 2rem;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;

      a {
        display: flex;
        align-items: center;
        gap: 1rem;
        padding: 1rem;
        color: rgba(255, 255, 255, 0.7);
        text-decoration: none;
        border-radius: 8px;
        transition: all 0.2s;

        &:hover {
          background-color: rgba(255, 255, 255, 0.1);
          color: white;
        }

        &.active {
          background-color: rgba(255, 255, 255, 0.1);
          color: white;
        }

        fa-icon {
          font-size: 1.2rem;
        }
      }
    }
  `]
})
export class SidebarComponent {
  faChartLine = faChartLine;
  faUser = faUser;
  faFileExport = faFileExport;

  constructor(private router: Router) {}
} 