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
import java.util.Objects;

/**
 * Endpoint you should upload to your binaries
 */
public class UploadEndpoint implements MaxSerializable {

    @NotNull
    private final @Valid String url;
    @Nullable
    private @Valid String token;

    @JsonCreator
    public UploadEndpoint(@JsonProperty("url") String url) { 
        this.url = url;
    }

    /**
    * URL to upload
    * @return url
    **/
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    public UploadEndpoint token(@Nullable String token) {
        this.setToken(token);
        return this;
    }

    /**
    * Video or audio token for send message
    * @return token
    **/
    @Nullable
    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
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

        UploadEndpoint other = (UploadEndpoint) o;
        return Objects.equals(this.url, other.url) &&
            Objects.equals(this.token, other.token);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UploadEndpoint{"
            + " url='" + url + '\''
            + " token='" + token + '\''
            + '}';
    }
}
