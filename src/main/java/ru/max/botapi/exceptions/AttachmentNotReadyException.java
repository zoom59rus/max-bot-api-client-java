package ru.max.botapi.exceptions;

public class AttachmentNotReadyException extends APIException {
    public AttachmentNotReadyException() {
        super("You cannot send message with unprocessed attachment (video in most cases). Please try after a " +
                "period of time. It depends on size of attachment.");
    }
}
