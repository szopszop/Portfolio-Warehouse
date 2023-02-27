package com.szymontracz.warehouse.service;

public class UsersServiceException extends RuntimeException {

    public UsersServiceException(){}

    public UsersServiceException(String message)
    {
        super(message);
    }

}