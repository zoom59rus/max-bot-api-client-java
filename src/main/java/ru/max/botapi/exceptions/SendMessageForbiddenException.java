package ru.max.botapi.exceptions;

public class SendMessageForbiddenException extends APIException {
    public SendMessageForbiddenException(String message) {
        super(message);
    }
}
