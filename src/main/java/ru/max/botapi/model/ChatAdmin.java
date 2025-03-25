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
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * Administrator id with permissions
 */
public class ChatAdmin implements MaxSerializable {

    @NotNull
    private final @Valid Long userId;
    @NotNull
    private final Set<@Valid ChatAdminPermission> permissions;

    @JsonCreator
    public ChatAdmin(@JsonProperty("user_id") Long userId, @JsonProperty("permissions") Set<ChatAdminPermission> permissions) { 
        this.userId = userId;
        this.permissions = permissions;
    }

    /**
    * @return userId
    **/
    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    /**
    * @return permissions
    **/
    @JsonProperty("permissions")
    public Set<ChatAdminPermission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ChatAdmin other = (ChatAdmin) o;
        return Objects.equals(this.userId, other.userId) &&
            Objects.equals(this.permissions, other.permissions);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatAdmin{"
            + " userId='" + userId + '\''
            + " permissions='" + permissions + '\''
            + '}';
    }
}
