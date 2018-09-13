import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShortenerComponent } from './components/shortener/shortener.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatIconModule } from '@angular/material'
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card'
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecentlinksComponent } from './components/recentlinks/recentlinks.component';
import { StorageServiceModule} from 'angular-webstorage-service';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatBadgeModule } from '@angular/material/badge';
import { ClipboardModule } from 'ngx-clipboard';

@NgModule({
  imports: [
    CommonModule, BrowserModule, ReactiveFormsModule, BrowserAnimationsModule, MatInputModule, MatFormFieldModule, 
    MatIconModule, FlexLayoutModule, HttpClientModule, MatListModule, MatCardModule, StorageServiceModule,
    MatButtonModule, ClipboardModule, MatTooltipModule, MatBadgeModule
  ],
  declarations: [ShortenerComponent, RecentlinksComponent],
  exports : [ShortenerComponent, RecentlinksComponent]
})
export class ShortlinkModule { }
