package com.kehutanan.superadmin.exception;

public class FileSizeLimitExceededException extends RuntimeException {
    
    public FileSizeLimitExceededException(String message) {
        super(message);
    }
    
    public FileSizeLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}