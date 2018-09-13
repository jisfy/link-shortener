import { Component, OnInit } from '@angular/core';
import { MatIconRegistry, MatIcon } from "@angular/material";
import { DomSanitizer } from "@angular/platform-browser";
import { ShortenerService } from '../../services/shortener.service';

@Component({
  selector: 'recent-links',
  templateUrl: './recentlinks.component.html',
  styleUrls: ['./recentlinks.component.css']
})
export class RecentlinksComponent implements OnInit {

  constructor(private shortenerService : ShortenerService, private domSanitizer : DomSanitizer,
    private matIconRegistry : MatIconRegistry) { 
      matIconRegistry.addSvgIcon('bar_chart', 
          domSanitizer.bypassSecurityTrustResourceUrl('assets/svg/outline-bar_chart-24px.svg'));
  }

  ngOnInit() {
    this.shortenerService.refreshShortLinks();
  }

  // Gets the latest links shortened with the service
  get links() {
    return this.shortenerService.shortLinks;
  }

}
