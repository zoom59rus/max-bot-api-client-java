package ru.max.botapi.exceptions;

public class ChatAccessForbiddenException extends APIException {
    public ChatAccessForbiddenException(String message) {
        super(403, message);
    }
}
