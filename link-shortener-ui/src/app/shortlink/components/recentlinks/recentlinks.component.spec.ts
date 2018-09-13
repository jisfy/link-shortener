import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, AbstractControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card'
import { MatListModule } from '@angular/material/list';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserModule, By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ClipboardModule } from 'ngx-clipboard';
import { MatTooltipModule } from '@angular/material/tooltip';

import { async, fakeAsync, tick, ComponentFixture, TestBed } from '@angular/core/testing';

import {DebugElement} from "@angular/core"; 
import { Observable, of, throwError} from 'rxjs';
import { HttpErrorResponse, HttpClientModule } from '@angular/common/http';
import { ERROR_COMPONENT_TYPE } from '@angular/compiler';
import { MatBadgeModule } from '@angular/material/badge';
import { MatIconModule } from '@angular/material/icon';

import { MatIconRegistry, MatIcon } from "@angular/material";
import { DomSanitizer } from "@angular/platform-browser";

import { RecentlinksComponent } from './recentlinks.component';
import { ShortenerService } from '../../services/shortener.service';
import { ShortLinkResource } from '../../core/shortlinkresource.model';

import { shortenerServiceStub, shortLink0, shortLink1, shortLink2 } from '../../mocks/shortlink.mocks';

const validShortLinks = [ shortLink0, shortLink1 ];
const additionalValidShortLink = shortLink2;

describe('RecentlinksComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecentlinksComponent ],
      imports: [
        CommonModule, BrowserModule, BrowserAnimationsModule, FlexLayoutModule, MatListModule, 
        MatCardModule, ClipboardModule, MatTooltipModule, MatBadgeModule, MatIconModule,
        HttpClientModule
      ],
      providers: [
        FormBuilder, {provide: ShortenerService, useValue: shortenerServiceStub}, MatIconRegistry 
      ]
    })
    .compileComponents();
  }));

  it('should show a card with no ShortLinks when created', () => {    
    let fixture = createFixtureForRecentLinksComponentWithShortenerService(shortenerServiceStub);
    shouldShowACardWithAllShortLinksComingFromtheService(fixture, shortenerServiceStub);
  });

  it('should show a card with all ShortLinks coming from the ShortenerService', () => {    
    shortenerServiceStub.shortLinks = validShortLinks;
    let fixture = createFixtureForRecentLinksComponentWithShortenerService(shortenerServiceStub);
    shouldShowACardWithAllShortLinksComingFromtheService(fixture, shortenerServiceStub);
  });

  it('should show a card with an additional ShortLink when one is added to the ShortenerService', () => {    
    shortenerServiceStub.shortLinks = validShortLinks;
    let fixture = createFixtureForRecentLinksComponentWithShortenerService(shortenerServiceStub);
    shouldShowACardWithAllShortLinksComingFromtheService(fixture, shortenerServiceStub);

    const numberOfShortLinksBeforeAddition = validShortLinks.length;
    let shortenerServiceInjected = TestBed.get(ShortenerService);
    shortenerServiceInjected.shortLinks.push(additionalValidShortLink);
    fixture.detectChanges();

    expectMatCardToBeValidAndListToHaveLength(fixture, numberOfShortLinksBeforeAddition + 1);
  });

  // Creates a RecenLinksComponent fixture overriding its injected ShortenerService
  function createFixtureForRecentLinksComponentWithShortenerService(shortenerService) {
    let fixture = TestBed.overrideProvider(ShortenerService, { useValue: shortenerService }).createComponent(RecentlinksComponent);    
    return fixture;
  }

  // Tests that the list of ShortLinks displayed by the RecentLinksComponent is right 
  // and has the correct number of list items
  function shouldShowACardWithAllShortLinksComingFromtheService(fixture, shortenerService) {
    let component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();      
    expectMatCardToBeValidAndListToHaveLength(fixture, shortenerService.shortLinks.length);
  }

  // Checks that the list of ShortLinks displayed by the RecentLinksComponent is right
  // and has the correct number of list items
  function expectMatCardToBeValidAndListToHaveLength(fixture, length) {
    let matCardElement = fixture.debugElement.query(By.css('mat-card'));
    let matCardSubtitleElement = fixture.debugElement.query(By.css('mat-card > mat-card-subtitle'));
    let matCardListElement = fixture.debugElement.query(By.css('mat-card > div#list'));    
    expect(matCardElement).toBeTruthy();
    expect(matCardSubtitleElement).toBeTruthy();
    expect(matCardListElement).toBeTruthy();
    expect(matCardListElement.children.length).toBe(length);
  }
  
});
