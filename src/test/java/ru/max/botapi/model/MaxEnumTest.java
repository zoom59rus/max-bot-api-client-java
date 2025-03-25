package ru.max.botapi.model;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import ru.max.botapi.UnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Category(UnitTest.class)
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