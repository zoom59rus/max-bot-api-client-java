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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jetbrains.annotations.Nullable;

/**
 * Request to attach reply keyboard to message
 */
public class ReplyKeyboardAttachmentRequest extends AttachmentRequest implements MaxSerializable {

    private @Valid Boolean direct;
    @Nullable
    private @Valid Long directUserId;
    @NotNull
    @Size(min = 1)
    private final List<List<@Valid ReplyButton>> buttons;

    @JsonCreator
    public ReplyKeyboardAttachmentRequest(@JsonProperty("buttons") List<List<ReplyButton>> buttons) { 
        super();
        this.buttons = buttons;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T map(Mapper<T> mapper) {
        return mapper.map(this);
    }

    public ReplyKeyboardAttachmentRequest direct(Boolean direct) {
        this.setDirect(direct);
        return this;
    }

    /**
    * Applicable only for chats. If &#x60;true&#x60; keyboard will be shown only for user bot mentioned or replied
    * @return direct
    **/
    @JsonProperty("direct")
    public Boolean isDirect() {
        return direct;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }

    public ReplyKeyboardAttachmentRequest directUserId(@Nullable Long directUserId) {
        this.setDirectUserId(directUserId);
        return this;
    }

    /**
    * If set to &#x60;true&#x60;, reply keyboard will only be shown to this participant in chat
    * @return directUserId
    **/
    @Nullable
    @JsonProperty("direct_user_id")
    public Long getDirectUserId() {
        return directUserId;
    }

    public void setDirectUserId(@Nullable Long directUserId) {
        this.directUserId = directUserId;
    }

    /**
    * Two-dimensional array of buttons
    * @return buttons
    **/
    @JsonProperty("buttons")
    public List<List<ReplyButton>> getButtons() {
        return buttons;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return AttachmentRequest.REPLY_KEYBOARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ReplyKeyboardAttachmentRequest other = (ReplyKeyboardAttachmentRequest) o;
        return Objects.equals(this.direct, other.direct) &&
            Objects.equals(this.directUserId, other.directUserId) &&
            Objects.equals(this.buttons, other.buttons);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (direct != null ? direct.hashCode() : 0);
        result = 31 * result + (directUserId != null ? directUserId.hashCode() : 0);
        result = 31 * result + (buttons != null ? buttons.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReplyKeyboardAttachmentRequest{"+ super.toString()
            + " direct='" + direct + '\''
            + " directUserId='" + directUserId + '\''
            + " buttons='" + buttons + '\''
            + '}';
    }
}
