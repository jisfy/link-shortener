import { Component } from '@angular/core';
import { ShortenerComponent } from './shortlink/components/shortener/shortener.component';
import { RecentlinksComponent } from './shortlink/components/recentlinks/recentlinks.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  constructor() {}
}

