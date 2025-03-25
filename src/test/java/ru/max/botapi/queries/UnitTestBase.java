package ru.max.botapi.queries;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.experimental.categories.Category;

import ru.max.botapi.MaxBotAPI;
import ru.max.botapi.UnitTest;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.client.impl.JacksonSerializer;
import ru.max.botapi.client.impl.OkHttpTransportClient;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatList;
import ru.max.botapi.server.MaxServer;
import ru.max.botapi.server.MaxService;


@Category(UnitTest.class)
public class UnitTestBase extends MaxService {
    protected static final AtomicLong ID_COUNTER = new AtomicLong();

    protected OkHttpTransportClient transport = new OkHttpTransportClient();
    JacksonSerializer serializer = new JacksonSerializer();
    public final MaxClient client = new MaxClient(MaxService.ACCESS_TOKEN, transport, serializer) {
        @Override
        public String getEndpoint() {
            return MaxServer.ENDPOINT;
        }
    };

    public final MaxBotAPI api = new MaxBotAPI(client);

    protected MaxClient invalidClient = new MaxClient("accesstoken", client.getTransport(), client.getSerializer()) {
        @Override
        public String getEndpoint() {
            return "https://invalid_endpoint";
        }
    };

    static {
        System.setProperty("max.botapi.endpoint", MaxServer.ENDPOINT);
        MaxServer.start();
    }

    public UnitTestBase() {
        super(new JacksonSerializer());
    }

    protected Chat randomChat() throws Exception {
        ChatList chatList = new GetChatsQuery(client).count(Integer.MAX_VALUE).execute();
        return chatList.getChats().get(ThreadLocalRandom.current().nextInt(0, chatList.getChats().size()));
    }
}
