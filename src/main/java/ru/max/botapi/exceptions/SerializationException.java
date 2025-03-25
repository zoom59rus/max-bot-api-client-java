package ru.max.botapi.exceptions;

public class SerializationException extends ClientException {
    public SerializationException(Exception e) {
        super(e);
    }
}
