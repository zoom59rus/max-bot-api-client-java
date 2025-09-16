package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.BotCommand;
import ru.max.botapi.model.BotInfo;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class GetMyInfoQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldGetMyInfo() throws Exception {
        BotInfo botInfo = new GetMyInfoQuery(client2).execute();
        List<BotCommand> expectedCommands = Arrays.asList(
                new BotCommand("cmd1").description("desc1"),
                new BotCommand("cmd2")
        );

        assertThat(botInfo.getCommands(), is(expectedCommands));
        assertThat(botInfo.getDescription(), is("test bot 2 description"));
    }
}