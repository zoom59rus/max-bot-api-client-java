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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Button
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = Button.class, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CallbackButton.class, name = Button.CALLBACK),
  @JsonSubTypes.Type(value = LinkButton.class, name = Button.LINK),
  @JsonSubTypes.Type(value = RequestGeoLocationButton.class, name = Button.REQUEST_GEO_LOCATION),
  @JsonSubTypes.Type(value = RequestContactButton.class, name = Button.REQUEST_CONTACT),
  @JsonSubTypes.Type(value = ChatButton.class, name = Button.CHAT),
})
@KnownInstance(ofClass = Button.class, discriminator = "type")
public class Button implements MaxSerializable {
    public static final String CALLBACK = "callback";
    public static final String LINK = "link";
    public static final String REQUEST_GEO_LOCATION = "request_geo_location";
    public static final String REQUEST_CONTACT = "request_contact";
    public static final String CHAT = "chat";
    public static final Set<String> TYPES = new HashSet<>(Arrays.asList(
        CALLBACK, 
        LINK, 
        REQUEST_GEO_LOCATION, 
        REQUEST_CONTACT, 
        CHAT
    ));

    @NotNull
    @Size(min = 1, max = 128)
    private final @Valid String text;

    @JsonCreator
    public Button(@JsonProperty("text") String text) { 
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

        Button other = (Button) o;
        return Objects.equals(this.text, other.text);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Button{"
            + " text='" + text + '\''
            + '}';
    }

    public interface Visitor {
        void visit(CallbackButton model);
        void visit(LinkButton model);
        void visit(RequestGeoLocationButton model);
        void visit(RequestContactButton model);
        void visit(ChatButton model);
        void visitDefault(Button model);
    }

    public interface Mapper<T> {
        T map(CallbackButton model);
        T map(LinkButton model);
        T map(RequestGeoLocationButton model);
        T map(RequestContactButton model);
        T map(ChatButton model);
        T mapDefault(Button model);
    }
}
