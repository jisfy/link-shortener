import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, AbstractControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatListModule } from '@angular/material/list';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule, By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { async, fakeAsync, tick, ComponentFixture, TestBed } from '@angular/core/testing';
import { ShortenerComponent } from './shortener.component';
import { ShortenerService } from '../../services/shortener.service';
import { ShortLinkResource } from '../../core/shortlinkresource.model';
import {DebugElement} from "@angular/core"; 
import { Observable, of, throwError} from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { ERROR_COMPONENT_TYPE } from '@angular/compiler';

import { shortenerServiceStub, validUrl, invalidUrl } from '../../mocks/shortlink.mocks';


describe('ShortenerComponent', () => {

  let component: ShortenerComponent;
  let fixture: ComponentFixture<ShortenerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShortenerComponent ],
      imports: [
        CommonModule, BrowserModule, ReactiveFormsModule, BrowserAnimationsModule, MatInputModule, MatFormFieldModule, 
        FlexLayoutModule, HttpClientModule, MatListModule        
      ],
      providers: [FormBuilder, {provide: ShortenerService, useValue: shortenerServiceStub}]
      
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShortenerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show a hint and no errors when created', () => {
    expect(component).toBeTruthy();
    let matHintElement = fixture.debugElement.query(By.css('mat-hint'));
    let matErrorElement = fixture.debugElement.query(By.css('mat-error'));
    expect(matHintElement).toBeTruthy();
    expect(matErrorElement).toBeFalsy();
    expect(matHintElement.nativeElement.textContent.trim()).toBe('Type in the URL and hit Enter');
  });

  it('should show an error when an incorrect URL is typed in', () => {
    expect(component).toBeTruthy();        

    let urlControl = component.urlForm.controls['urlControl']
    typeInSomeUrl(urlControl, invalidUrl);

    expect(urlControl.valid).toBeFalsy();
    let matErrorContentElement = fixture.debugElement.query(By.css('mat-error > span'));
    expect(matErrorContentElement).toBeTruthy();
    expect(matErrorContentElement.nativeElement.innerHTML).toMatch('Malformed.*http://hostname\\[:port\\]/\\[path\\]');
  });

  it('should show an error when the API server is down and the URL typed is valid', () => {
    let theservice = TestBed.get(ShortenerService);
    spyOn(theservice, 'shortenLink').and.returnValue(throwError(
      new HttpErrorResponse({status: 404, statusText : 'Cant reach destination'})));

    let urlControl = component.urlForm.controls['urlControl']
    typeInSomeUrl(urlControl, validUrl);
    expect(urlControl.valid).toBeTruthy();
  
    let matErrorContentElement = fixture.debugElement.query(By.css('div > mat-error'));
    expect(matErrorContentElement).toBeTruthy();
    expect(matErrorContentElement.nativeElement.innerHTML).toMatch('Backend Error! .*');
  });

  it('should clear the urlControl when the API server is up and the URL typed is valid', () => {
    let theservice = TestBed.get(ShortenerService);
  
    let urlControl = component.urlForm.controls['urlControl']
    typeInSomeUrl(urlControl, validUrl);
    expect(urlControl.valid).toBeTruthy();
  
    let matErrorContentElement = fixture.debugElement.query(By.css('div > mat-error'));
    expect(matErrorContentElement).toBeFalsy();
    expect(urlControl.value).toBe('');
  });
  
  function typeInSomeUrl(urlControl : AbstractControl, typedInUrl : string) {
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component).toBeTruthy();        
    urlControl.setValue(typedInUrl);

    let textAreaElement : DebugElement = fixture.debugElement.query(By.css('textarea'));
    textAreaElement.triggerEventHandler('keydown.enter', { preventDefault : function () {}});
    fixture.detectChanges();
  };   
});
