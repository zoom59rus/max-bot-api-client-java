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
import java.util.List;
import java.util.Objects;

/**
 * Schema representing body of message
 */
public class MessageBody implements MaxSerializable {

    @NotNull
    private final @Valid String mid;
    @NotNull
    private final @Valid Long seq;
    @Nullable
    private final @Valid String text;
    @Nullable
    private final List<@Valid Attachment> attachments;
    @Nullable
    private List<@Valid MarkupElement> markup;

    @JsonCreator
    public MessageBody(@JsonProperty("mid") String mid, @JsonProperty("seq") Long seq, @Nullable @JsonProperty("text") String text, @Nullable @JsonProperty("attachments") List<Attachment> attachments) { 
        this.mid = mid;
        this.seq = seq;
        this.text = text;
        this.attachments = attachments;
    }

    /**
    * Unique identifier of message
    * @return mid
    **/
    @JsonProperty("mid")
    public String getMid() {
        return mid;
    }

    /**
    * Sequence identifier of message in chat
    * @return seq
    **/
    @JsonProperty("seq")
    public Long getSeq() {
        return seq;
    }

    /**
    * Message text
    * @return text
    **/
    @Nullable
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
    * Message attachments. Could be one of &#x60;Attachment&#x60; type. See description of this schema
    * @return attachments
    **/
    @Nullable
    @JsonProperty("attachments")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public MessageBody markup(@Nullable List<MarkupElement> markup) {
        this.setMarkup(markup);
        return this;
    }

    /**
    * Message text markup. See [Formatting](#section/About/Text-formatting) section for more info
    * @return markup
    **/
    @Nullable
    @JsonProperty("markup")
    public List<MarkupElement> getMarkup() {
        return markup;
    }

    public void setMarkup(@Nullable List<MarkupElement> markup) {
        this.markup = markup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        MessageBody other = (MessageBody) o;
        return Objects.equals(this.mid, other.mid) &&
            Objects.equals(this.seq, other.seq) &&
            Objects.equals(this.text, other.text) &&
            Objects.equals(this.attachments, other.attachments) &&
            Objects.equals(this.markup, other.markup);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (mid != null ? mid.hashCode() : 0);
        result = 31 * result + (seq != null ? seq.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (markup != null ? markup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageBody{"
            + " mid='" + mid + '\''
            + " seq='" + seq + '\''
            + " text='" + text + '\''
            + " attachments='" + attachments + '\''
            + " markup='" + markup + '\''
            + '}';
    }
}
