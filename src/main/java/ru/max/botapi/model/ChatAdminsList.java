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
import java.util.List;
import java.util.Objects;


/**
 * ChatAdminsList
 */
public class ChatAdminsList implements MaxSerializable {

    @NotNull
    private final List<@Valid ChatAdmin> admins;

    @JsonCreator
    public ChatAdminsList(@JsonProperty("admins") List<ChatAdmin> admins) { 
        this.admins = admins;
    }

    /**
    * @return admins
    **/
    @JsonProperty("admins")
    public List<ChatAdmin> getAdmins() {
        return admins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ChatAdminsList other = (ChatAdminsList) o;
        return Objects.equals(this.admins, other.admins);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (admins != null ? admins.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatAdminsList{"
            + " admins='" + admins + '\''
            + '}';
    }
}
