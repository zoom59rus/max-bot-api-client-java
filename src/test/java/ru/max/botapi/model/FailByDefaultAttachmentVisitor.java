package ru.max.botapi.model;

import static org.junit.jupiter.api.Assertions.fail;


public class FailByDefaultAttachmentVisitor implements Attachment.Visitor {
    @Override
    public void visit(PhotoAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(VideoAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(AudioAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(FileAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(StickerAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(ContactAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(InlineKeyboardAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(ReplyKeyboardAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(ShareAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(LocationAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visit(DataAttachment model) {
        fail("Should not happens");
    }

    @Override
    public void visitDefault(Attachment model) {
        fail("Should not happens");
    }
}
