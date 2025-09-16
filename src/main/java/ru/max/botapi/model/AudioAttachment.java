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
 * AudioAttachment
 */
public class AudioAttachment extends Attachment implements MaxSerializable {

    @NotNull
    private final @Valid MediaAttachmentPayload payload;
    @Nullable
    private @Valid String transcription;

    @JsonCreator
    public AudioAttachment(@JsonProperty("payload") MediaAttachmentPayload payload) { 
        super();
        this.payload = payload;
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
    * @return payload
    **/
    @JsonProperty("payload")
    public MediaAttachmentPayload getPayload() {
        return payload;
    }

    public AudioAttachment transcription(@Nullable String transcription) {
        this.setTranscription(transcription);
        return this;
    }

    /**
    * Audio transcription
    * @return transcription
    **/
    @Nullable
    @JsonProperty("transcription")
    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(@Nullable String transcription) {
        this.transcription = transcription;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return Attachment.AUDIO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        AudioAttachment other = (AudioAttachment) o;
        return Objects.equals(this.payload, other.payload) &&
            Objects.equals(this.transcription, other.transcription);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (transcription != null ? transcription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AudioAttachment{"+ super.toString()
            + " payload='" + payload + '\''
            + " transcription='" + transcription + '\''
            + '}';
    }
}
