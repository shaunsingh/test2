import { Routes } from '@angular/router';
import { OverviewComponent } from './pages/overview/overview.component';
import { AccountsComponent } from './pages/accounts/accounts.component';
import { ExportComponent } from './pages/export/export.component';

export const routes: Routes = [
  { path: '', redirectTo: '/overview', pathMatch: 'full' },
  { path: 'overview', component: OverviewComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: 'export', component: ExportComponent }
]; 