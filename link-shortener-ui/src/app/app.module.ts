import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { ShortlinkModule } from './shortlink/shortlink.module';
import { RecentlinksComponent } from './shortlink/components/recentlinks/recentlinks.component';
import { ShortenerComponent } from './shortlink/components/shortener/shortener.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule, ShortlinkModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
