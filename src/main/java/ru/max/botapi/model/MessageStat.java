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
 * Message statistics
 */
public class MessageStat implements MaxSerializable {

    @NotNull
    private final @Valid Integer views;

    @JsonCreator
    public MessageStat(@JsonProperty("views") Integer views) { 
        this.views = views;
    }

    /**
    * @return views
    **/
    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        MessageStat other = (MessageStat) o;
        return Objects.equals(this.views, other.views);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (views != null ? views.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageStat{"
            + " views='" + views + '\''
            + '}';
    }
}
