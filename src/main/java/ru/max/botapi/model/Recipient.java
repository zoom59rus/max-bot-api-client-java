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
import java.util.Objects;

/**
 * New message recipient. Could be user or chat
 */
public class Recipient implements MaxSerializable {

    @Nullable
    private final @Valid Long chatId;
    @NotNull
    private final @Valid ChatType chatType;
    @Nullable
    private final @Valid Long userId;

    @JsonCreator
    public Recipient(@Nullable @JsonProperty("chat_id") Long chatId, @JsonProperty("chat_type") ChatType chatType, @Nullable @JsonProperty("user_id") Long userId) { 
        this.chatId = chatId;
        this.chatType = chatType;
        this.userId = userId;
    }

    /**
    * Chat identifier
    * @return chatId
    **/
    @Nullable
    @JsonProperty("chat_id")
    public Long getChatId() {
        return chatId;
    }

    /**
    * Chat type
    * @return chatType
    **/
    @JsonProperty("chat_type")
    public ChatType getChatType() {
        return chatType;
    }

    /**
    * User identifier, if message was sent to user
    * @return userId
    **/
    @Nullable
    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        Recipient other = (Recipient) o;
        return Objects.equals(this.chatId, other.chatId) &&
            Objects.equals(this.chatType, other.chatType) &&
            Objects.equals(this.userId, other.userId);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (chatType != null ? chatType.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recipient{"
            + " chatId='" + chatId + '\''
            + " chatType='" + chatType + '\''
            + " userId='" + userId + '\''
            + '}';
    }
}
