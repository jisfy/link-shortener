import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { MatFormField, MatError } from '@angular/material/form-field'
import { MatIcon } from '@angular/material/icon'
import { MatGridList, MatGridTile } from '@angular/material'
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { ShortLinkResource } from '../../core/shortlinkresource.model';
import { ShortenerService } from '../../services/shortener.service';

@Component({
  selector: 'link-shortener',
  templateUrl: './shortener.component.html',
})
export class ShortenerComponent implements OnInit {

  private static readonly urlRegExPattern : string = 
      '(https?://)(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)';

  urlForm : FormGroup;
  isValidUrl : boolean;
  isShorteningOperationFailed : boolean;
  serviceErrorMessage : string;  

  constructor(private formBuilder : FormBuilder, private shortenerService : ShortenerService) {
    this.isValidUrl = true;
    this.isShorteningOperationFailed = false;
    this.urlForm = formBuilder.group({
      urlControl : ['', Validators.pattern(ShortenerComponent.urlRegExPattern) ]
    });    
  }

  ngOnInit() {    
  }

  // Gets the value of the ShorteningOperationFailed flag
  get operationFailed() : boolean {
    return this.isShorteningOperationFailed;
  }

  // Triggers the URL shorten operation, getting the URL value from the 'urlControl'
  shorten(event: Event) {
    event.preventDefault();    
    this.isShorteningOperationFailed = false;    
    const urlControl : AbstractControl = this.getUrlControl();    
    this.forceControlValidation(urlControl);
    this.shortenUrl(urlControl);     
  }

  // Shortens the URL taken from the control value
  private shortenUrl(control : AbstractControl) {
    this.isValidUrl = control.valid;
    if (this.isValidUrl) {
      this.shortenerService.shortenLink(control.value)      
          .subscribe(r => control.setValue(""), this.handleError.bind(this));        
    }        
  }

  // Gets the URL Control from the Form
  private getUrlControl() : AbstractControl {
    const urlControl : AbstractControl = this.urlForm.controls['urlControl'];    
    return urlControl;
  }

  // Forces the Control validation, since at times validation does not happen normally. 
  // For instance when event propagation is aborted
  private forceControlValidation(control : AbstractControl) {
    control.markAsTouched();
    control.updateValueAndValidity();
  }

  // Handles a ShortenerService error, logging to console and setting the flag 'ShorteningOperationFailed' 
  // I don't like at all that the underlying Http surfaces here. The ShortenerService shouldn't leak
  // any Http related data
  private handleError(error: HttpErrorResponse) {
    this.isShorteningOperationFailed = true;
    if (error.error instanceof ErrorEvent) {
      this.serviceErrorMessage = error.error.message;
      console.error('An error occurred:', error.error.message);      
    } else {
      this.serviceErrorMessage = `Backend returned code ${error.status}`;
      console.error(this.serviceErrorMessage);
    }
  };
}


