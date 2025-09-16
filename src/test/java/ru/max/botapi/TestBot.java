package ru.max.botapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.BotInfo;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.UpdateList;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static ru.max.botapi.MaxIntegrationTest.info;


public class TestBot {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final MaxBotAPI api;
    private final Thread poller;
    private final Thread consumerThread;
    private final BotInfo me;
    private final String hashedName;

    private final AtomicBoolean isStopped = new AtomicBoolean();
    private final Map<Long, List<Update.Visitor>> consumers = new ConcurrentHashMap<>();
    private final BlockingQueue<Update> updates = new ArrayBlockingQueue<>(100);
    private final AtomicReference<Long> marker = new AtomicReference<>();

    TestBot(MaxClient botClient, boolean isCi) throws APIException, ClientException {
        this.api = new MaxBotAPI(botClient);
        this.me = api.getMyInfo().execute();
        this.hashedName = isCi ? MaxIntegrationTest.randomText(16) : me.getName();
        this.poller = new Thread(this::poll, "updates-poller-" + hashedName);
        this.consumerThread = new Thread(this::consumeUpdates, "updates-consumer-" + hashedName);
    }

    public Long getUserId() {
        return getBotInfo().getUserId();
    }

    public String getAvatarUrl() {
        return getBotInfo().getAvatarUrl();
    }

    public String getUsername() {
        return getBotInfo().getUsername();
    }

    public String getFirstName() {
        return getBotInfo().getFirstName();
    }

    public BotInfo getBotInfo() {
        return me;
    }

    public void start() {
        poller.start();
        consumerThread.start();
        LOG.info("Bot " + hashedName + " started");
    }

    public AutoCloseable addConsumer(long chatId, Update.Visitor consumer) {
        List<Update.Visitor> chatConsumers = consumers.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>());
        chatConsumers.add(consumer);
        return () -> removeConsumer(chatId, consumer);
    }

    public void removeConsumer(long chatId, Update.Visitor consumer) {
        consumers.getOrDefault(chatId, Collections.emptyList()).remove(consumer);
    }

    public void sendToMaster(String message) {
        NewMessageBody msg = new NewMessageBody(message, null, null);
        try {
            api.sendMessage(msg).userId(Long.valueOf(System.getenv("MAX_BOTAPI_MASTER_ID"))).execute();
        } catch (APIException | ClientException e) {
            LOG.error("Failed to send message to master", e);
        }
    }

    private void consumeUpdates() {
        while (true) {
            Update update;
            try {
                update = updates.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Long chatId = update.map(GetChatId.INSTANCE);
            List<Update.Visitor> chatConsumers = consumers.getOrDefault(chatId, Collections.emptyList());
            for (Update.Visitor consumer : chatConsumers) {
                update.visit(consumer);
            }
        }
    }

    private void poll() {
        do {
            marker.set(pollOnce(marker.get()));
        } while (!isStopped.get());
    }

    private Long pollOnce(Long marker) {
        int error = 0;
        try {
            UpdateList updateList = api.getUpdates().marker(marker).timeout(5).execute();
            if (Thread.currentThread().isInterrupted()) {
                return updateList.getMarker();
            }

            for (Update update : updateList.getUpdates()) {
                updates.offer(update);
                info("Bot " + hashedName + " got update: {}", update);
            }

            error = 0;
            return updateList.getMarker();
        } catch (APIException | ClientException e) {
            if (e.getCause() instanceof InterruptedException) {
                return marker;
            }

            error++;
            LOG.error("Failed to get updates, marker: {}", marker, e);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(Math.min(error, 5)));
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }

        return marker;
    }

    @Override
    public String toString() {
        return me.toString();
    }
}
