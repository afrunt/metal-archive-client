package com.afrunt.metalarchive.api;

/**
 * @author Andrii Frunt
 */
public class MetalArchiveClientException extends RuntimeException {
    public MetalArchiveClientException() {
        super();
    }

    public MetalArchiveClientException(String message) {
        super(message);
    }

    public MetalArchiveClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetalArchiveClientException(Throwable cause) {
        super(cause);
    }
}
