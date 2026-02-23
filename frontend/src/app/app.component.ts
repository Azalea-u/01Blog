import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
    <div class="crt-root">
      <div class="crt-vignette"></div>
      <app-navbar />
      <main class="terminal-container page-content">
        <router-outlet />
      </main>
    </div>
  `
})
export class AppComponent {}
