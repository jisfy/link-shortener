package com.chompchompfig.linkshortener.domain;

/**
 * A Validator that performs simple checks, like not null
 */
public class SimpleValidator {

    public static final String INVALID_NULL_URL_ERROR_MSG = "invalid URL, can't be null";

    /**
     * Validates an object for not null
     *
     * @param argument <p>to object to validate</p>
     * @param errorMessage <p>the error message in case the validation fails</p>
     */
    public void notNull(Object argument, String errorMessage) {
        if (argument == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
