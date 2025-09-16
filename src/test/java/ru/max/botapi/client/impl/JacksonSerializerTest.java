package ru.max.botapi.client.impl;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.model.MessageBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@Tag("UnitTest")
public class JacksonSerializerTest {
    private JacksonSerializer serializer = new JacksonSerializer();

    @Test
    public void shouldThrowSerializationException() throws Exception {
        serializer.serialize(new NotSerializableClass());
    }

    @Test
    public void shouldThrowSerializationException2() throws Exception {
        serializer.serializeToString(new NotSerializableClass());
    }

    @Test
    public void shouldThrowSerializationExceptionOnDeserialization() throws Exception {
        serializer.deserialize("{", MessageBody.class);
    }

    @Test
    public void shouldThrowSerializationExceptionOnDeserialization2() throws Exception {
        serializer.deserialize(new byte[]{1, 2, 3}, MessageBody.class);
    }

    @Test
    public void shouldThrowSerializationExceptionOnDeserialization3() throws Exception {
        serializer.deserialize(new ByteArrayInputStream(new byte[]{1, 2, 3}), MessageBody.class);
    }

    @Test
    public void shouldReturnNullOnEmptyInput() throws Exception {
        assertThat(serializer.deserialize((byte[]) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize((String) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize((InputStream) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize("", Object.class), is(nullValue()));
    }

    @Test
    public void shouldReturnNullOnSerialize() throws Exception {
        assertThat(serializer.serialize(null), is(nullValue()));
        assertThat(serializer.serializeToString(null), is(nullValue()));
    }

    @Test
    public void testSerializeToString() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        String serialized = serializer.serializeToString(object);
        MessageBody deserialized = serializer.deserialize(serialized, MessageBody.class);
        assertThat(deserialized, is(object));
    }

    @Test
    public void testSerialize() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        byte[] serialized = serializer.serialize(object);
        MessageBody deserialized = serializer.deserialize(serialized, MessageBody.class);
        assertThat(deserialized, is(object));
    }

    @Test
    public void testDeserializeStream() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        byte[] serialized = serializer.serialize(object);
        MessageBody deserialized = serializer.deserialize(new ByteArrayInputStream(serialized), MessageBody.class);
        assertThat(deserialized, is(object));
    }

    private static class NotSerializableClass {
        private final NotSerializableClass self = this;

        @Override
        public String toString() {
            return self.getClass().getName();
        }
    }
}