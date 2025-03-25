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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jetbrains.annotations.Nullable;

/**
 * After pressing this type of button client will send a message on behalf of user with given payload
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = ReplyButton.class, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = SendMessageButton.class, name = ReplyButton.MESSAGE),
  @JsonSubTypes.Type(value = SendGeoLocationButton.class, name = ReplyButton.USER_GEO_LOCATION),
  @JsonSubTypes.Type(value = SendContactButton.class, name = ReplyButton.USER_CONTACT),
})
@KnownInstance(ofClass = ReplyButton.class, discriminator = "type")
public class ReplyButton implements MaxSerializable {
    public static final String MESSAGE = "message";
    public static final String USER_GEO_LOCATION = "user_geo_location";
    public static final String USER_CONTACT = "user_contact";
    public static final Set<String> TYPES = new HashSet<>(Arrays.asList(
        MESSAGE, 
        USER_GEO_LOCATION, 
        USER_CONTACT
    ));

    @NotNull
    @Size(min = 1, max = 128)
    private final @Valid String text;
    @Nullable
    @Size(max = 1024)
    private @Valid String payload;

    @JsonCreator
    public ReplyButton(@JsonProperty("text") String text) { 
        this.text = text;
    }



    public void visit(Visitor visitor) {
        visitor.visitDefault(this);
    }

    public <T> T map(Mapper<T> mapper) {
        return mapper.mapDefault(this);
    }

    /**
    * Visible text of button
    * @return text
    **/
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public ReplyButton payload(@Nullable String payload) {
        this.setPayload(payload);
        return this;
    }

    /**
    * Button payload
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

    @JsonProperty("type")
    public String getType() {
        throw new UnsupportedOperationException("Model has no concrete type.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ReplyButton other = (ReplyButton) o;
        return Objects.equals(this.text, other.text) &&
            Objects.equals(this.payload, other.payload);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReplyButton{"
            + " text='" + text + '\''
            + " payload='" + payload + '\''
            + '}';
    }

    public interface Visitor {
        void visit(SendMessageButton model);
        void visit(SendGeoLocationButton model);
        void visit(SendContactButton model);
        void visitDefault(ReplyButton model);
    }

    public interface Mapper<T> {
        T map(SendMessageButton model);
        T map(SendGeoLocationButton model);
        T map(SendContactButton model);
        T mapDefault(ReplyButton model);
    }
}
