
import { Observable, of, throwError } from 'rxjs';
import { ShortLinkResource } from '../core/shortlinkresource.model';

// a ShortenerService stub returning a preconfigured ShortLinkResource
export const shortenerServiceStub = {
    shortLinks : [],
    shortenLink(longUrl : string) : Observable<ShortLinkResource> {
      let result = of({
        linkId: "12345",
        shortURL: "http://localhost:9090/v/12345",
        longURL: "http://www.osoco.com",
        visits : 0
      });
      return result;    
    }, 
    refreshShortLinks() {}
  };
  

// a WebStorageService stub with empty get and set operations
export const webStorageServiceStub = {
    get(key: String) { },
    set(key: String, value: any) { }
}

// an invalid long URL
export const invalidUrl = 'h://www.osoco.com';

// a valid long URL
export const validUrl = 'http://www.osoco.com';

// a valid ShortLinkResource instance. This is, one with a valid long URL
export const validUrlShortLinkResource = {
    linkId: "12345",
    shortURL: "http://localhost:9090/v/12345",
    longURL: validUrl,
    visits: 0
};

// a bunch a short link identifiers for testing
export const shortLinkId0 = buildShortLinkId(1);
export const shortLinkId1 = buildShortLinkId(2);
export const shortLinkId2 = buildShortLinkId(3);

// a short links identifiers request string for the api
export const shortLinksIdsString = shortLinkId0 + ":" + shortLinkId1 + ":" + shortLinkId2;

// a bunch of short links for testing
export const shortLink0 = buildShortLink(1);
export const shortLink1 = buildShortLink(2);
export const shortLink2 = buildShortLink(3);

// a target number of visits to update
export const shortLink2UpdatedVisits = 2;

// an ShortLinkResource with an updated number of visits
export const visitsUpdatedShortLink2 = {
    linkId: shortLink2.linkId,
    shortURL: shortLink2.shortURL,
    longURL: shortLink2.longURL,
    visits: shortLink2UpdatedVisits
}

// a list of ShortLinkResources with whose visits have been updated
export const visitUpdatedShortLinkResources = buildShortLinkResources([shortLink0, shortLink1, visitsUpdatedShortLink2]);

// builds a ShortLinkResources instance with the given list of ShortLinkResource's
export function buildShortLinkResources(shortLinkResources: ShortLinkResource[]) {
    const shortLinkResourcesBuilt = { "_embedded": { "ex:shortLinks": shortLinkResources } };
    return shortLinkResourcesBuilt;
}

// builds an empty ShortLinkResources instance
export function buildEmptyShortLinkResources() {
    return {};
}

// builds a ShortLink identifier conforming to the pattern 1234{number} given an index
export function buildShortLinkId(index: number) {
    const shortLinkIdPrefix = '1234';
    const shortLinkId = `${shortLinkIdPrefix}${index}`;
    return shortLinkId;
}

// builds a fake ShortLink for testing
export function buildShortLink(index: number) {
    const shortLinkId = buildShortLinkId(index);
    const shortLinkShortURLPrefix = 'http://localhost:9090/v/';
    const shortLinkShortURL = `${shortLinkShortURLPrefix}${shortLinkId}`;
    const shortLinkLongPrefix = 'http://www.osoco.com/';
    const shortLinkLongURL = `${shortLinkLongPrefix}${index}`;
    const shortLinkVisits = 0;

    const shortLink = {
        linkId: shortLinkId,
        shortURL: shortLinkShortURL,
        longURL: shortLinkLongURL,
        visits: shortLinkVisits
    };

    return shortLink;
}