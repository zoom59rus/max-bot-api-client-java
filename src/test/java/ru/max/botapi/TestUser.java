package ru.max.botapi;

import org.jetbrains.annotations.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ru.max.botapi.model.User;


public class TestUser extends User {
    @JsonCreator
    public TestUser(@JsonProperty("user_id") Long userId, @Nullable @JsonProperty("first_name") String firstName,
                    @Nullable @JsonProperty("last_name") String lastName, @Nullable @JsonProperty("username") String username, @JsonProperty("is_bot") Boolean isBot,
                    @JsonProperty("last_activity_time") Long lastActivityTime) {
        super(userId, firstName, lastName, username, isBot, lastActivityTime);
    }
}
