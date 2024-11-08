package com.tutorialapi.rest.exceptions;

/**
 * Error response object
 * @param status - HTTP status code
 * @param message - Error message
 */
public record ErrorResponse(int status, String message) {
}
