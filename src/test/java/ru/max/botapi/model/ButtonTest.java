package ru.max.botapi.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Tag("UnitTest")
public class ButtonTest {
    @Test
    public void shouldVisitDefault() {
        Button button = new Button("text");
        button.visit(new FailByDefaultButtonVisitor() {
            @Override
            public void visitDefault(Button model) {
                assertThat(model, is(button));
            }
        });
    }

    @Test
    public void shouldMapDefault() {
        Button button = new Button("asd");
        button.map(new FailByDefaultButtonMapper<Void>() {
            @Override
            public Void mapDefault(Button model) {
                assertThat(model, is(button));
                return null;
            }
        });
    }

    @Test
    public void shouldMap() {
        List<Button> buttons = Arrays.asList(
                new CallbackButton("payload", "text"),
                new LinkButton("url", "text"),
                new RequestGeoLocationButton("text geo"),
                new CallbackButton("payload", "text"),
                new ChatButton("title", "text").startPayload("startPayload").chatDescription("description").uuid(1234),
                new RequestContactButton("request contact"));

        for (Button button : buttons) {
            AtomicReference<Button> mapped = button.map(new FailByDefaultButtonMapper<AtomicReference<Button>>() {
                @Override
                public AtomicReference<Button> map(CallbackButton model) {
                    return new AtomicReference<>(button);
                }

                @Override
                public AtomicReference<Button> map(LinkButton model) {
                    return new AtomicReference<>(button);
                }

                @Override
                public AtomicReference<Button> map(RequestGeoLocationButton model) {
                    return new AtomicReference<>(button);
                }

                @Override
                public AtomicReference<Button> map(RequestContactButton model) {
                    return new AtomicReference<>(button);
                }

                @Override
                public AtomicReference<Button> map(ChatButton model) {
                    return new AtomicReference<>(button);
                }
            });

            assertThat(mapped.get(), is(button));
        }
    }
}