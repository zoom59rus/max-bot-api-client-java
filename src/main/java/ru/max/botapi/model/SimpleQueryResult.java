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
 * Simple response to request
 */
public class SimpleQueryResult implements MaxSerializable {

    @NotNull
    private final @Valid Boolean success;
    private @Valid String message;

    @JsonCreator
    public SimpleQueryResult(@JsonProperty("success") Boolean success) { 
        this.success = success;
    }

    /**
    * &#x60;true&#x60; if request was successful. &#x60;false&#x60; otherwise
    * @return success
    **/
    @JsonProperty("success")
    public Boolean isSuccess() {
        return success;
    }

    public SimpleQueryResult message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
    * Explanatory message if the result is not successful
    * @return message
    **/
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        SimpleQueryResult other = (SimpleQueryResult) o;
        return Objects.equals(this.success, other.success) &&
            Objects.equals(this.message, other.message);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (success != null ? success.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleQueryResult{"
            + " success='" + success + '\''
            + " message='" + message + '\''
            + '}';
    }
}
