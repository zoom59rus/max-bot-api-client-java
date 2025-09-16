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
 * FileAttachment
 */
public class FileAttachment extends Attachment implements MaxSerializable {

    @NotNull
    private final @Valid FileAttachmentPayload payload;
    @NotNull
    private final @Valid String filename;
    @NotNull
    private final @Valid Long size;

    @JsonCreator
    public FileAttachment(@JsonProperty("payload") FileAttachmentPayload payload, @JsonProperty("filename") String filename, @JsonProperty("size") Long size) { 
        super();
        this.payload = payload;
        this.filename = filename;
        this.size = size;
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
    public FileAttachmentPayload getPayload() {
        return payload;
    }

    /**
    * Uploaded file name
    * @return filename
    **/
    @JsonProperty("filename")
    public String getFilename() {
        return filename;
    }

    /**
    * File size in bytes
    * @return size
    **/
    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return Attachment.FILE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        FileAttachment other = (FileAttachment) o;
        return Objects.equals(this.payload, other.payload) &&
            Objects.equals(this.filename, other.filename) &&
            Objects.equals(this.size, other.size);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileAttachment{"+ super.toString()
            + " payload='" + payload + '\''
            + " filename='" + filename + '\''
            + " size='" + size + '\''
            + '}';
    }
}
