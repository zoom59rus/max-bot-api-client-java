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


/**
 * Custom reply keyboard in message
 */
public class ReplyKeyboardAttachment extends Attachment implements MaxSerializable {

    @NotNull
    private final List<List<@Valid ReplyButton>> buttons;

    @JsonCreator
    public ReplyKeyboardAttachment(@JsonProperty("buttons") List<List<ReplyButton>> buttons) { 
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

    /**
    * @return buttons
    **/
    @JsonProperty("buttons")
    public List<List<ReplyButton>> getButtons() {
        return buttons;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return Attachment.REPLY_KEYBOARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ReplyKeyboardAttachment other = (ReplyKeyboardAttachment) o;
        return Objects.equals(this.buttons, other.buttons);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (buttons != null ? buttons.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReplyKeyboardAttachment{"+ super.toString()
            + " buttons='" + buttons + '\''
            + '}';
    }
}
