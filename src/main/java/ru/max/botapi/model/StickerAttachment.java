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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * StickerAttachment
 */
public class StickerAttachment extends Attachment implements MaxSerializable {

    @NotNull
    private final @Valid StickerAttachmentPayload payload;
    @NotNull
    private final @Valid Integer width;
    @NotNull
    private final @Valid Integer height;

    @JsonCreator
    public StickerAttachment(@JsonProperty("payload") StickerAttachmentPayload payload, @JsonProperty("width") Integer width, @JsonProperty("height") Integer height) { 
        super();
        this.payload = payload;
        this.width = width;
        this.height = height;
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
    public StickerAttachmentPayload getPayload() {
        return payload;
    }

    /**
    * Sticker width
    * @return width
    **/
    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    /**
    * Sticker height
    * @return height
    **/
    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return Attachment.STICKER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        StickerAttachment other = (StickerAttachment) o;
        return Objects.equals(this.payload, other.payload) &&
            Objects.equals(this.width, other.width) &&
            Objects.equals(this.height, other.height);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StickerAttachment{"+ super.toString()
            + " payload='" + payload + '\''
            + " width='" + width + '\''
            + " height='" + height + '\''
            + '}';
    }
}
