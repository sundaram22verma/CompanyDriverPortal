package com.test.CompanyDriverPortal.globalException;

// This class defines a custom exception called ResourceNotFoundException, which extends RuntimeException. It is used to indicate that a requested resource was not found in the application. The class provides two constructors: one that accepts a message and another that accepts both a message and a cause (another throwable). This allows for more detailed error handling and better debugging when this exception is thrown in the application.
public class ResourceNotFoundException extends RuntimeException {

    // Custom exception for resource not found scenarios. This exception can be thrown when a requested resource (like a user, product, etc.) is not found in the database or any data source. It extends RuntimeException, so it is an unchecked exception and does not require explicit handling in the code where it is thrown.
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Overloaded constructor to allow chaining of exceptions. This can be useful when you want to provide more context about the original cause of the exception while still indicating that a resource was not found.
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}