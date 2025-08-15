package org.example.queuemehospital.Api;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
