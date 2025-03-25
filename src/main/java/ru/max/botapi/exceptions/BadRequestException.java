package ru.max.botapi.exceptions;

public class BadRequestException extends APIException {
    BadRequestException(String message) {
        super(message);
    }
}
