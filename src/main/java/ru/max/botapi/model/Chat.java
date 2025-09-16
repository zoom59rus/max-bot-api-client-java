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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Chat
 */
public class Chat implements MaxSerializable {

    @NotNull
    private final @Valid Long chatId;
    @NotNull
    private final @Valid ChatType type;
    @NotNull
    private final @Valid ChatStatus status;
    @Nullable
    private final @Valid String title;
    @Nullable
    private final @Valid Image icon;
    @NotNull
    private final @Valid Long lastEventTime;
    @NotNull
    private final @Valid Integer participantsCount;
    @Nullable
    private @Valid Long ownerId;
    @Nullable
    private Map<String, @Valid Long> participants;
    @NotNull
    private final @Valid Boolean isPublic;
    @Nullable
    private @Valid String link;
    @Nullable
    private final @Valid String description;
    @Nullable
    private @Valid UserWithPhoto dialogWithUser;
    @Nullable
    private @Valid Integer messagesCount;
    @Nullable
    private @Valid String chatMessageId;
    @Nullable
    private @Valid Message pinnedMessage;

    @JsonCreator
    public Chat(@JsonProperty("chat_id") Long chatId, @JsonProperty("type") ChatType type, @JsonProperty("status") ChatStatus status, @Nullable @JsonProperty("title") String title, @Nullable @JsonProperty("icon") Image icon, @JsonProperty("last_event_time") Long lastEventTime, @JsonProperty("participants_count") Integer participantsCount, @JsonProperty("is_public") Boolean isPublic, @Nullable @JsonProperty("description") String description) { 
        this.chatId = chatId;
        this.type = type;
        this.status = status;
        this.title = title;
        this.icon = icon;
        this.lastEventTime = lastEventTime;
        this.participantsCount = participantsCount;
        this.isPublic = isPublic;
        this.description = description;
    }

    /**
    * Chats identifier
    * @return chatId
    **/
    @JsonProperty("chat_id")
    public Long getChatId() {
        return chatId;
    }

    /**
    * Type of chat. One of: dialog, chat, channel
    * @return type
    **/
    @JsonProperty("type")
    public ChatType getType() {
        return type;
    }

    /**
    * Chat status. One of:  - active: bot is active member of chat  - removed: bot was kicked  - left: bot intentionally left chat  - closed: chat was closed  - suspended: bot was stopped by user. *Only for dialogs*
    * @return status
    **/
    @JsonProperty("status")
    public ChatStatus getStatus() {
        return status;
    }

    /**
    * Visible title of chat. Can be null for dialogs
    * @return title
    **/
    @Nullable
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
    * Icon of chat
    * @return icon
    **/
    @Nullable
    @JsonProperty("icon")
    public Image getIcon() {
        return icon;
    }

    /**
    * Time of last event occurred in chat
    * @return lastEventTime
    **/
    @JsonProperty("last_event_time")
    public Long getLastEventTime() {
        return lastEventTime;
    }

    /**
    * Number of people in chat. Always 2 for &#x60;dialog&#x60; chat type
    * @return participantsCount
    **/
    @JsonProperty("participants_count")
    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public Chat ownerId(@Nullable Long ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    /**
    * Identifier of chat owner. Visible only for chat admins
    * @return ownerId
    **/
    @Nullable
    @JsonProperty("owner_id")
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(@Nullable Long ownerId) {
        this.ownerId = ownerId;
    }

    public Chat participants(@Nullable Map<String, Long> participants) {
        this.setParticipants(participants);
        return this;
    }

    public Chat putParticipantsItem(String key, Long participantsItem) {
        if (this.participants == null) {
            this.participants = new HashMap<String, Long>();
        }

        this.participants.put(key, participantsItem);
        return this;
    }

    /**
    * Participants in chat with time of last activity. Can be *null* when you request list of chats. Visible for chat admins only
    * @return participants
    **/
    @Nullable
    @JsonProperty("participants")
    public Map<String, Long> getParticipants() {
        return participants;
    }

    public void setParticipants(@Nullable Map<String, Long> participants) {
        this.participants = participants;
    }

    /**
    * Is current chat publicly available. Always &#x60;false&#x60; for dialogs
    * @return isPublic
    **/
    @JsonProperty("is_public")
    public Boolean isPublic() {
        return isPublic;
    }

    public Chat link(@Nullable String link) {
        this.setLink(link);
        return this;
    }

    /**
    * Link on chat
    * @return link
    **/
    @Nullable
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    public void setLink(@Nullable String link) {
        this.link = link;
    }

    /**
    * Chat description
    * @return description
    **/
    @Nullable
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public Chat dialogWithUser(@Nullable UserWithPhoto dialogWithUser) {
        this.setDialogWithUser(dialogWithUser);
        return this;
    }

    /**
    * Another user in conversation. For &#x60;dialog&#x60; type chats only
    * @return dialogWithUser
    **/
    @Nullable
    @JsonProperty("dialog_with_user")
    public UserWithPhoto getDialogWithUser() {
        return dialogWithUser;
    }

    public void setDialogWithUser(@Nullable UserWithPhoto dialogWithUser) {
        this.dialogWithUser = dialogWithUser;
    }

    public Chat messagesCount(@Nullable Integer messagesCount) {
        this.setMessagesCount(messagesCount);
        return this;
    }

    /**
    * Messages count in chat. Only for group chats and channels. **Not available** for dialogs
    * @return messagesCount
    **/
    @Nullable
    @JsonProperty("messages_count")
    public Integer getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(@Nullable Integer messagesCount) {
        this.messagesCount = messagesCount;
    }

    public Chat chatMessageId(@Nullable String chatMessageId) {
        this.setChatMessageId(chatMessageId);
        return this;
    }

    /**
    * Identifier of message that contains &#x60;chat&#x60; button initialized chat
    * @return chatMessageId
    **/
    @Nullable
    @JsonProperty("chat_message_id")
    public String getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(@Nullable String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public Chat pinnedMessage(@Nullable Message pinnedMessage) {
        this.setPinnedMessage(pinnedMessage);
        return this;
    }

    /**
    * Pinned message in chat or channel. Returned only when single chat is requested
    * @return pinnedMessage
    **/
    @Nullable
    @JsonProperty("pinned_message")
    public Message getPinnedMessage() {
        return pinnedMessage;
    }

    public void setPinnedMessage(@Nullable Message pinnedMessage) {
        this.pinnedMessage = pinnedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        Chat other = (Chat) o;
        return Objects.equals(this.chatId, other.chatId) &&
            Objects.equals(this.type, other.type) &&
            Objects.equals(this.status, other.status) &&
            Objects.equals(this.title, other.title) &&
            Objects.equals(this.icon, other.icon) &&
            Objects.equals(this.lastEventTime, other.lastEventTime) &&
            Objects.equals(this.participantsCount, other.participantsCount) &&
            Objects.equals(this.ownerId, other.ownerId) &&
            Objects.equals(this.participants, other.participants) &&
            Objects.equals(this.isPublic, other.isPublic) &&
            Objects.equals(this.link, other.link) &&
            Objects.equals(this.description, other.description) &&
            Objects.equals(this.dialogWithUser, other.dialogWithUser) &&
            Objects.equals(this.messagesCount, other.messagesCount) &&
            Objects.equals(this.chatMessageId, other.chatMessageId) &&
            Objects.equals(this.pinnedMessage, other.pinnedMessage);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (lastEventTime != null ? lastEventTime.hashCode() : 0);
        result = 31 * result + (participantsCount != null ? participantsCount.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (isPublic != null ? isPublic.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dialogWithUser != null ? dialogWithUser.hashCode() : 0);
        result = 31 * result + (messagesCount != null ? messagesCount.hashCode() : 0);
        result = 31 * result + (chatMessageId != null ? chatMessageId.hashCode() : 0);
        result = 31 * result + (pinnedMessage != null ? pinnedMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Chat{"
            + " chatId='" + chatId + '\''
            + " type='" + type + '\''
            + " status='" + status + '\''
            + " title='" + title + '\''
            + " icon='" + icon + '\''
            + " lastEventTime='" + lastEventTime + '\''
            + " participantsCount='" + participantsCount + '\''
            + " ownerId='" + ownerId + '\''
            + " participants='" + participants + '\''
            + " isPublic='" + isPublic + '\''
            + " link='" + link + '\''
            + " description='" + description + '\''
            + " dialogWithUser='" + dialogWithUser + '\''
            + " messagesCount='" + messagesCount + '\''
            + " chatMessageId='" + chatMessageId + '\''
            + " pinnedMessage='" + pinnedMessage + '\''
            + '}';
    }
}
