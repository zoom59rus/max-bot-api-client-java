package ru.max.botapi;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ru.max.botapi.model.User;

/**
 * Deserializes user object ignoring last_activity_time property
 */
public class UserTestDeserializer extends StdDeserializer<User> {
    protected UserTestDeserializer() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        User user = jsonParser.readValueAs(TestUser.class);
        return new User(user.getUserId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.isBot(),
                MaxIntegrationTest.LAST_ACTIVITY_IGNORED);
    }
}
