package ru.max.botapi.model;

import static org.junit.jupiter.api.Assertions.fail;


public class FailByDefaultARVisitor implements AttachmentRequest.Visitor {
    @Override
    public void visit(PhotoAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(VideoAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(AudioAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(FileAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(StickerAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(ContactAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(InlineKeyboardAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(ReplyKeyboardAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(LocationAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visit(ShareAttachmentRequest model) {
        visitDefault(model);
    }

    @Override
    public void visitDefault(AttachmentRequest model) {
        fail("Should not happens");
    }
}
