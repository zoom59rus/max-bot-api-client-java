package ru.max.botapi.markup;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class MarkupIntegrationTest extends MaxIntegrationTest {
    private Chat chat;
    private Chat dialog;
    private String plainText;
    private List<MarkupElement> expectedMarkup;
    private List<MarkupElement> expectedDialogMarkup;

    @BeforeEach
    public void setUp() throws Exception {
        chat = getByTitle(getChats(), "SendMarkupIntegrationTest");
        dialog = getByType(getChats(), ChatType.DIALOG);
        plainText = readFile("plaintext.txt");
        expectedMarkup = expectedMarkup(false);
        expectedDialogMarkup = expectedMarkup(true);
    }

    @Test
    public void shouldNotParseMarkup() throws Exception {
        _shouldNotParse("markup.md");
        _shouldNotParse("markup.html");
    }

    @Test
    public void shouldParseMarkup() throws Exception {
        _shouldParse("markup.md", TextFormat.MARKDOWN);
        _shouldParse("markup.html", TextFormat.HTML);
    }

    @Test
    public void shouldNotParseOnEdit() throws Exception {
        _shouldNotParseOnEdit("markup.md");
        _shouldNotParseOnEdit("markup.html");
    }

    @Test
    public void shouldParseOnEdit() throws Exception {
        _shouldParseOnEdit("markup.md", TextFormat.MARKDOWN);
        _shouldParseOnEdit("markup.html", TextFormat.HTML);
    }

    private static UserMentionMarkup mentionMarkup(int from, boolean inDialog, int length) {
        UserMentionMarkup markup = new UserMentionMarkup(from, length);
        if (inDialog) {
            markup.setUserLink("userlink");
        } else {
            markup.setUserId(762619020L);
        }
        return markup;
    }

    private static List<MarkupElement> expectedMarkup(boolean inDialog) {
        return Arrays.asList(
                new StrongMarkup(0, 11),
                new EmphasizedMarkup(0, 11),
                new StrongMarkup(13, 6),
                new EmphasizedMarkup(13, 14),
                new EmphasizedMarkup(29, 4),
                new StrongMarkup(29, 14),
                new MonospacedMarkup(45, 10),
                new MonospacedMarkup(57, 3),
                new MonospacedMarkup(66, 6),
                new MonospacedMarkup(157, 5),
                new MonospacedMarkup(209, 4),
                new LinkMarkup("/uri", 215, 4),
                new LinkMarkup("/uri", 221, 4),
                new LinkMarkup("(foo)and(bar)", 233, 4),
                mentionMarkup(239, inDialog, 8),
                mentionMarkup(249, inDialog, 8),
                mentionMarkup(288, inDialog, 8),
                mentionMarkup(306, inDialog, 8),
                mentionMarkup(321, inDialog, 8),
                new StrongMarkup(321, 8),
                mentionMarkup(345, inDialog, 8),
                new StrongMarkup(345, 8),
                new EmphasizedMarkup(345, 8),
                new MonospacedMarkup(431, 8),
                new MonospacedMarkup(459, 39),
                new UserMentionMarkup(519, 7).userId(762619020L),
                new HighlightedMarkup(533, 4));
    }

    private void verify(TextFormat format, Message message, boolean isDialog) {
        assertThat(format.getValue(), message.getBody().getText(), is(plainText));
        assertThat(format.getValue(), message.getBody().getMarkup(),
                is(isDialog ? expectedDialogMarkup : expectedMarkup));
    }

    private void _shouldParseOnEdit(String inputFile, TextFormat format) throws Exception {
        String textWithMarkup = readFile(inputFile);
        for (Chat chat : Arrays.asList(chat, dialog)) {
            info("Chat: " + chat.getChatId());
            SendMessageResult result = botAPI.sendMessage(new NewMessageBody(randomText(), null, null))
                    .chatId(chat.getChatId())
                    .execute();

            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(new NewMessageBody(textWithMarkup, null, null)
                    .format(format), messageId)
                    .execute();

            Message editedMessage = botAPI.getMessageById(messageId).execute();
            verify(format, editedMessage, chat.getType() == ChatType.DIALOG);
        }
    }

    private void _shouldNotParseOnEdit(String inputFile) throws Exception {
        String textWithMarkup = readFile(inputFile);

        for (Chat chat : Arrays.asList(chat, dialog)) {
            info("Chat: " + chat.getChatId());
            SendMessageResult result = botAPI.sendMessage(new NewMessageBody(randomText(), null, null))
                    .chatId(chat.getChatId())
                    .execute();

            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(new NewMessageBody(textWithMarkup, null, null), messageId).execute();

            Message editedMessage = botAPI.getMessageById(messageId).execute();
            assertThat(editedMessage.getBody().getText(), is(textWithMarkup));
            assertThat(editedMessage.getBody().getMarkup(), is(nullValue()));
        }
    }

    private void _shouldParse(String inputFile, TextFormat format) throws Exception {
        String text = readFile(inputFile);
        for (Chat chat : Arrays.asList(chat, dialog)) {
            info("Chat: " + chat.getChatId());
            SendMessageResult result = botAPI.sendMessage(new NewMessageBody(text, null, null)
                    .format(format))
                    .chatId(chat.getChatId())
                    .execute();

            verify(format, result.getMessage(), chat.getType() == ChatType.DIALOG);
        }
    }

    private void _shouldNotParse(String markupFile) throws Exception {
        String text = readFile(markupFile);
        for (Chat chat : Arrays.asList(chat, dialog)) {
            info("Chat: " + chat.getChatId());
            SendMessageResult result = botAPI.sendMessage(new NewMessageBody(text, null, null))
                    .chatId(chat.getChatId())
                    .execute();

            assertThat(result.getMessage().getBody().getText(), is(text));
            assertThat(result.getMessage().getBody().getMarkup(), is(nullValue()));
        }
    }

    private String readFile(String name) throws IOException {
        return IOUtils.toString(getClass().getClassLoader().getResourceAsStream("markup/" + name),
                StandardCharsets.UTF_8);
    }
}
