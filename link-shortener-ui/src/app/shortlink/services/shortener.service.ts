import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ShortLinkResource, ShortLinkResources } from '../core/shortlinkresource.model';
import { Observable, of } from 'rxjs';
import { tap, map, catchError} from 'rxjs/operators';
import {LOCAL_STORAGE, WebStorageService} from 'angular-webstorage-service';


// A Stateful Service to shorten URLs. The Service maintains a cache of the recently shortened links .
// The maximum number of shortened links kept in the cache is given by the maxRecentLinks constant.
// In order for the cache to be maintained across browser restarts the HTML5 localstorage facility
// is used
@Injectable({
  providedIn: 'root'
})
export class ShortenerService {

  static readonly SHORT_LINKS_API_URL : string = '/api/shortlinks';
  static readonly SHORT_LINKS_IDS_API_URL : string = ShortenerService.SHORT_LINKS_API_URL + '/ids/';
  static readonly LOCAL_STORAGE_LINKS_KEY : string = 'links';
  static readonly SHORTLINKS_COLLECTION_REL_NAME = 'ex:shortLinks';
  static readonly HAL_EMBEDDED_COLLECTION_PROPERTY_NAME = '_embedded';
  static readonly MAX_RECENT_LINKS : number = 3;

  shortLinks : ShortLinkResource[];

  // Builds a new ShortenerService instance
  constructor(private httpClient : HttpClient, @Inject(LOCAL_STORAGE) private storage: WebStorageService) { 
    let fetchedLinks : ShortLinkResource[] = this.storage.get(ShortenerService.LOCAL_STORAGE_LINKS_KEY);
    this.shortLinks = [];
    if (fetchedLinks != null) {
      this.shortLinks = fetchedLinks;
    }    
  }

  // Shortens the given URL 
  shortenLink(longUrl : string) : Observable<ShortLinkResource> {
    let result : Observable<ShortLinkResource> = 
        this.httpClient.post<ShortLinkResource>(ShortenerService.SHORT_LINKS_API_URL, longUrl).pipe(
          tap(this.handleShortLinks.bind(this))
        );
    return result;    
  } 

  // Stores a new ShortLink in the cache, respecting its max number of items
  private handleShortLinks(shortLinkResource : ShortLinkResource) {
    if (this.shortLinks.length >= ShortenerService.MAX_RECENT_LINKS) {
      this.shortLinks.shift();      
    } 
    this.shortLinks.push(shortLinkResource);
    this.storage.set(ShortenerService.LOCAL_STORAGE_LINKS_KEY, this.shortLinks);
  }

  refreshShortLinks() {
    let fetchedLinks : ShortLinkResource[] = this.storage.get(ShortenerService.LOCAL_STORAGE_LINKS_KEY);
    let fetchedLinksIdsString = fetchedLinks.map(slr => slr.linkId).join(":");
    this.httpClient.get<ShortLinkResources>(
      ShortenerService.SHORT_LINKS_IDS_API_URL + fetchedLinksIdsString).subscribe(sls => this.handleRefresh(sls));
  }

  private handleRefresh(shortLinkResourcesFetched) {
    if (shortLinkResourcesFetched.hasOwnProperty(ShortenerService.HAL_EMBEDDED_COLLECTION_PROPERTY_NAME)) {
      let shortLinkResources : ShortLinkResource[] = 
        shortLinkResourcesFetched._embedded[ShortenerService.SHORTLINKS_COLLECTION_REL_NAME];
      let shortLinkResourcesIds : string[] = shortLinkResources.map(sl => sl.linkId);
      let refreshableShortLinks = this.shortLinks.filter(sl => shortLinkResourcesIds.includes(sl.linkId));
      refreshableShortLinks.forEach(rsl => {
        rsl.visits = shortLinkResources.find(sl => sl.linkId == rsl.linkId).visits;
      });
      this.storage.set(ShortenerService.LOCAL_STORAGE_LINKS_KEY, this.shortLinks);
    }    
  }
}
