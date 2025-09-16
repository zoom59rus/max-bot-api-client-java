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
 * ChatMembersList
 */
public class ChatMembersList implements MaxSerializable {

    @NotNull
    private final List<@Valid ChatMember> members;
    @Nullable
    private @Valid Long marker;

    @JsonCreator
    public ChatMembersList(@JsonProperty("members") List<ChatMember> members) { 
        this.members = members;
    }

    /**
    * Participants in chat with time of last activity. Visible only for chat admins
    * @return members
    **/
    @JsonProperty("members")
    public List<ChatMember> getMembers() {
        return members;
    }

    public ChatMembersList marker(@Nullable Long marker) {
        this.setMarker(marker);
        return this;
    }

    /**
    * Pointer to the next data page
    * @return marker
    **/
    @Nullable
    @JsonProperty("marker")
    public Long getMarker() {
        return marker;
    }

    public void setMarker(@Nullable Long marker) {
        this.marker = marker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ChatMembersList other = (ChatMembersList) o;
        return Objects.equals(this.members, other.members) &&
            Objects.equals(this.marker, other.marker);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (marker != null ? marker.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatMembersList{"
            + " members='" + members + '\''
            + " marker='" + marker + '\''
            + '}';
    }
}
