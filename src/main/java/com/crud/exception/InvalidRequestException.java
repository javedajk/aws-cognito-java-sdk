package com.crud.exception;

public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = 4088649120307193208L;

    public InvalidRequestException(String error) {
        super(error);
    }
}
