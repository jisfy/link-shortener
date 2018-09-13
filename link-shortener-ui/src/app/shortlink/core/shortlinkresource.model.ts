// A ShortLink resource representation
export interface ShortLinkResource {
    linkId: string; // the ShortLink identifier
    shortURL: string; // the shortened URL
    longURL: string;  // the original long URL that was shortened
    visits : number; // the number of visits of this ShortLink
}

// Represents an abriged version of an application/json+hal list of ShortLinkResources
export interface ShortLinkResources {
    _embedded: {
        "ex:shortLinks" : [ShortLinkResource] // the list of ShortLink resources
    }
}