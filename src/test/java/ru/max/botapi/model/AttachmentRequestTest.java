package ru.max.botapi.model;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import ru.max.botapi.UnitTest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Category(UnitTest.class)
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