package com.example.blog.exception;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found");
    }
}
