package ru.max.botapi.queries;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.FailByDefaultUpdateVisitor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class BotStartedUpdateIntegrationTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdate() throws Exception {
        String payload = randomText();
        CountDownLatch updateReceived = new CountDownLatch(1);
        FailByDefaultUpdateVisitor consumer = new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(BotStartedUpdate model) {
                assertThat(model.getUser().getUserId(), is(MaxIntegrationTest.bot3.getUserId()));
                updateReceived.countDown();
            }
        };

        long chatId = MaxIntegrationTest.bot1.getUserId() ^ MaxIntegrationTest.bot3.getUserId();

        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(chatId, consumer)) {
            MaxIntegrationTest.bot3.startAnotherBot(MaxIntegrationTest.bot1.getUserId(), payload);
            MaxIntegrationTest.await(updateReceived);
        }
    }

    @Test
    public void shouldGetWebhookUpdate() throws Exception {
        String payload = randomText();
        CountDownLatch updateReceived = new CountDownLatch(1);
        Bot1ToBot3RedirectingUpdateVisitor consumer = new Bot1ToBot3RedirectingUpdateVisitor(
                new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
                    @Override
                    public void visit(BotStartedUpdate model) {
                        assertThat(model.getUser().getUserId(), is(MaxIntegrationTest.bot1.getUserId()));
                        updateReceived.countDown();
                    }
                });

        long chatId = MaxIntegrationTest.bot1.getUserId() ^ MaxIntegrationTest.bot3.getUserId();
        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(chatId, consumer)) {
            MaxIntegrationTest.bot3.startYourself(MaxIntegrationTest.bot1.getUserId(), payload);
            MaxIntegrationTest.await(updateReceived, 10);
        }
    }
}
