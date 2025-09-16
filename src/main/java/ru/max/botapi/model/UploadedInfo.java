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

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.Objects;


/**
 * This is information you will receive as soon as audio/video is uploaded
 */
public class UploadedInfo implements MaxSerializable {

    private @Valid String token;

    public UploadedInfo token(String token) {
        this.setToken(token);
        return this;
    }

    /**
    * Token is unique uploaded media identifier
    * @return token
    **/
    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        UploadedInfo other = (UploadedInfo) o;
        return Objects.equals(this.token, other.token);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UploadedInfo{"
            + " token='" + token + '\''
            + '}';
    }
}
