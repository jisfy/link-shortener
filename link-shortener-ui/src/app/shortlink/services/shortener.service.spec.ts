import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { TestBed, getTestBed } from '@angular/core/testing';
import {LOCAL_STORAGE, WebStorageService} from 'angular-webstorage-service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Observable, of, throwError} from 'rxjs';

import { ShortenerService } from './shortener.service';
import { ShortLinkResource, ShortLinkResources } from '../core/shortlinkresource.model';

import { webStorageServiceStub, invalidUrl, validUrl, validUrlShortLinkResource } from '../mocks/shortlink.mocks'
import { buildShortLink, buildShortLinkId, buildShortLinkResources, buildEmptyShortLinkResources } from '../mocks/shortlink.mocks'
import { shortLink0, shortLink1, shortLink2, shortLinkId0, shortLinkId1, shortLinkId2, shortLinksIdsString } from '../mocks/shortlink.mocks'
import { visitUpdatedShortLinkResources, visitsUpdatedShortLink2} from '../mocks/shortlink.mocks'


describe('ShortenerService', () => {

  let injector: TestBed;
  let service: ShortenerService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [ShortenerService, {provide: LOCAL_STORAGE, useValue: webStorageServiceStub}]      
    });
    injector = getTestBed();
    service = injector.get(ShortenerService);
    httpMock = injector.get(HttpTestingController);
  });
  
  afterEach(() => {
    httpMock.verify();
  });

  describe('#shortenLink', () => {
    it('should return an Observable<ShortLinkResource> when the API server is up', () => {
        let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
        let webStorageStubSpy = spyOn(webStorageServiceInjected, 'set').and.callFake(function () {
            expect(arguments[0]).toBe(ShortenerService.LOCAL_STORAGE_LINKS_KEY);
            expect(arguments[1].length).toBe(1);
        });

        service.shortenLink(validUrl).subscribe(shortLink => {            
            expect(shortLink).toEqual(validUrlShortLinkResource);
        });
        
        const req = httpMock.expectOne(ShortenerService.SHORT_LINKS_API_URL);
        expect(req.request.method).toBe("POST");
        expect(req.request.body).toBe(validUrl);
        req.flush(validUrlShortLinkResource);

        expect(webStorageStubSpy).toHaveBeenCalled();
    });

    it('should return a failed Observable<ShortLinkResource> when the API server is down', () => {
        let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
        let webStorageStubSpy = spyOn(webStorageServiceInjected, 'set');

        service.shortenLink(validUrl).subscribe(shortLink => {}, error => {
            expect(error).toBeTruthy();
            expect(service.shortLinks.length).toBe(0);
        });
        
        const req = httpMock.expectOne(ShortenerService.SHORT_LINKS_API_URL);
        expect(req.request.method).toBe("POST");
        expect(req.request.body).toBe(validUrl);        
        req.error(new ErrorEvent('http'), {status : 404, statusText: 'Cant reach destination'});
        
        expect(webStorageStubSpy.calls.count()).toBe(0);
    });

    it('should overflow the shortlinks when a new link is shortened and current links is already 3', () => {
        const filledShortLinks = [shortLink0, shortLink1, shortLink2];
        service.shortLinks = filledShortLinks;
        expect(service.shortLinks.length).toBe(3);
        
        let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
        let webStorageStubSpy = spyOn(webStorageServiceInjected, 'set').and.callFake(function () {
            expect(arguments[0]).toBe(ShortenerService.LOCAL_STORAGE_LINKS_KEY);
            expect(arguments[1].length).toBe(3);
            expect(arguments[1][0]).toBe(shortLink1);
            expect(arguments[1][1]).toBe(shortLink2);
            expect(arguments[1][2]).toBe(validUrlShortLinkResource);
        });

        service.shortenLink(validUrl).subscribe(shortLink => {            
            expect(shortLink).toEqual(validUrlShortLinkResource);
        });
        
        const req = httpMock.expectOne(ShortenerService.SHORT_LINKS_API_URL);
        expect(req.request.method).toBe("POST");
        expect(req.request.body).toBe(validUrl);
        req.flush(validUrlShortLinkResource);

        expect(webStorageStubSpy).toHaveBeenCalled();
    });
  });

  describe('#refreshShortLinks', () => {
   
    it('should leave the ShortLinks outdated when the API server is down', () => {
      const filledShortLinks = [shortLink0, shortLink1, shortLink2];
      let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
      let webStorageStubSpy = spyOn(webStorageServiceInjected, 'get').and.returnValue(filledShortLinks);

      service.refreshShortLinks();
        
      const req = httpMock.expectOne(ShortenerService.SHORT_LINKS_IDS_API_URL + shortLinksIdsString);
      expect(req.request.method).toBe("GET");   
      req.error(new ErrorEvent('http'), {status : 404, statusText: 'Cant reach destination'});
        
      expect(webStorageStubSpy.calls.count()).toBe(1);
    });   

    it('should update the ShortLinks when the API server is up', () => {     
     const filledShortLinks = [shortLink0, shortLink1, shortLink2];
     const expectedStorageSetShortLinkResources = [shortLink0, shortLink1, visitsUpdatedShortLink2];
     shouldUpdateShortLinksWhenApiServerReturnsResources(filledShortLinks, filledShortLinks, expectedStorageSetShortLinkResources, 
      visitUpdatedShortLinkResources);
    });

    it('should update no ShortLinks when the API server is slow and the ShortLinks in the Service have changed', () => {      
      const filledShortLinks = [shortLink0, shortLink1, shortLink2];
      const moreRecentShortLinks = [ buildShortLink(4), buildShortLink(5), buildShortLink(6) ];
      shouldUpdateShortLinksWhenApiServerReturnsResources(moreRecentShortLinks, filledShortLinks, moreRecentShortLinks, 
        visitUpdatedShortLinkResources);
    });

    it('should update no ShortLinks when the API server is up but didnt find the requested resources', () => {
      const filledShortLinks = [shortLink0, shortLink1, shortLink2];
      service.shortLinks = filledShortLinks;
      let webStorageStubSpy = setUpLocalStorageForGetToReturn(filledShortLinks);

      let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
      let webStorageStubSpySet = spyOn(webStorageServiceInjected, 'set');

      service.refreshShortLinks();
      
      const serviceReturnedShortLinkResources = buildEmptyShortLinkResources();
      setUpHttpClientGetToReturn(filledShortLinks, serviceReturnedShortLinkResources);
      
      expect(webStorageStubSpy.calls.count()).toBe(1);
      expect(webStorageStubSpySet.calls.count()).toBe(0);
     });

    function setUpLocalStorageForGetToReturn(storageGetShortLinkResources : ShortLinkResource[]) {
      let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
      let webStorageStubSpy = spyOn(webStorageServiceInjected, 'get').and.returnValue(storageGetShortLinkResources);
      return webStorageStubSpy;
    }

    function setUpHttpClientGetToReturn(serviceRequestedShortLinkResources : ShortLinkResource[], serviceReturnedShortLinkResources) {
      const serviceRequestedShortLinksIds = serviceRequestedShortLinkResources.map(sl => sl.linkId).join(":");
      const req = httpMock.expectOne(ShortenerService.SHORT_LINKS_IDS_API_URL + serviceRequestedShortLinksIds);
      expect(req.request.method).toBe("GET");   
      req.flush(serviceReturnedShortLinkResources);
    }

    function shouldUpdateShortLinksWhenApiServerReturnsResources(serviceShortLinks : ShortLinkResource[], 
        storageGetShortLinkResources : ShortLinkResource[], expectedStorageSetShortLinkResources : ShortLinkResource[], 
            serviceReturnedShortLinkResources) {
      service.shortLinks = serviceShortLinks;
      let webStorageStubSpy = setUpLocalStorageForGetToReturn(storageGetShortLinkResources);

      let webStorageServiceInjected = TestBed.get(LOCAL_STORAGE);
      let webStorageStubSpySet = spyOn(webStorageServiceInjected, 'set').and.callFake(function () {
        expect(arguments[0]).toBe(ShortenerService.LOCAL_STORAGE_LINKS_KEY);
        expect(arguments[1].length).toBe(expectedStorageSetShortLinkResources.length);        
        expect(arguments[1]).toEqual(expectedStorageSetShortLinkResources);
      });

      service.refreshShortLinks();
      
      setUpHttpClientGetToReturn(storageGetShortLinkResources, serviceReturnedShortLinkResources);
      
      expect(webStorageStubSpy.calls.count()).toBe(1);
      expect(webStorageStubSpySet.calls.count()).toBe(1);
    }
  });

});
