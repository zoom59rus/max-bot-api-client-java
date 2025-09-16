package ru.max.botapi.model;






import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("UnitTest")
public class MaxEnumTest {
    @Test
    public void shouldCreate() {
        ChatType chatType = MaxEnum.create(ChatType.class, "dialog");
        assertThat(chatType, is(ChatType.DIALOG));
    }

    @Test
    public void shouldReturnNull() {
        ChatType chatType = MaxEnum.create(ChatType.class, null);
        assertThat(chatType, is(nullValue()));
    }
}