/*
 * ------------------------------------------------------------------------
 * Max chat Bot API
 * ------------------------------------------------------------------------
 * Copyright (C) 2025 COMMUNICATION PLATFORM LLC
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package ru.max.botapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Bot gets this type of update as soon as user pressed &#x60;Start&#x60; button
 */
public class BotStartedUpdate extends Update implements MaxSerializable {

    @NotNull
    private final @Valid Long chatId;
    @NotNull
    private final @Valid User user;
    @Nullable
    @Size(max = 512)
    private @Valid String payload;
    private @Valid String userLocale;

    @JsonCreator
    public BotStartedUpdate(@JsonProperty("chat_id") Long chatId, @JsonProperty("user") User user, @JsonProperty("timestamp") Long timestamp) { 
        super(timestamp);
        this.chatId = chatId;
        this.user = user;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T map(Mapper<T> mapper) {
        return mapper.map(this);
    }

    /**
    * Dialog identifier where event has occurred
    * @return chatId
    **/
    @JsonProperty("chat_id")
    public Long getChatId() {
        return chatId;
    }

    /**
    * User pressed the &#39;Start&#39; button
    * @return user
    **/
    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    public BotStartedUpdate payload(@Nullable String payload) {
        this.setPayload(payload);
        return this;
    }

    /**
    * Additional data from deep-link passed on bot startup
    * @return payload
    **/
    @Nullable
    @JsonProperty("payload")
    public String getPayload() {
        return payload;
    }

    public void setPayload(@Nullable String payload) {
        this.payload = payload;
    }

    public BotStartedUpdate userLocale(String userLocale) {
        this.setUserLocale(userLocale);
        return this;
    }

    /**
    * Current user locale in IETF BCP 47 format
    * @return userLocale
    **/
    @JsonProperty("user_locale")
    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    @JsonProperty("update_type")
    @Override
    public String getType() {
        return Update.BOT_STARTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        BotStartedUpdate other = (BotStartedUpdate) o;
        return Objects.equals(this.chatId, other.chatId) &&
            Objects.equals(this.user, other.user) &&
            Objects.equals(this.payload, other.payload) &&
            Objects.equals(this.userLocale, other.userLocale) &&
            super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (userLocale != null ? userLocale.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BotStartedUpdate{"+ super.toString()
            + " chatId='" + chatId + '\''
            + " user='" + user + '\''
            + " payload='" + payload + '\''
            + " userLocale='" + userLocale + '\''
            + '}';
    }
}
