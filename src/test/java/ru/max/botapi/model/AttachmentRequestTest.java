package ru.max.botapi.model;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@Tag("UnitTest")
public class AttachmentRequestTest {
    @Test
    public void shouldVisitDefault() {
        AttachmentRequest attachmentRequest = new AttachmentRequest();
        attachmentRequest.visit(new FailByDefaultARVisitor() {
            @Override
            public void visitDefault(AttachmentRequest model) {
                assertThat(model, is(attachmentRequest));
            }
        });
    }

}