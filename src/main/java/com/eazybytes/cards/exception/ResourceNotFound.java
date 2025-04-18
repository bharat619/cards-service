package com.eazybytes.cards.exception;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with given data %s: %s", resourceName, fieldName, fieldValue));
    }
}
