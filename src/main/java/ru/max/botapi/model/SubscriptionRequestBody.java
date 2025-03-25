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
import javax.validation.constraints.Pattern;


/**
 * Request to set up WebHook subscription
 */
public class SubscriptionRequestBody implements MaxSerializable {

    @NotNull
    private final @Valid String url;
    @Pattern(regexp = "^[a-zA-Z0-9_-]{5,256}$")
    private @Valid String secret;
    private Set<@Valid String> updateTypes;
    private @Valid String version;

    @JsonCreator
    public SubscriptionRequestBody(@JsonProperty("url") String url) { 
        this.url = url;
    }

    /**
    * URL of HTTP(S)-endpoint of your bot. Must starts with http(s)://
    * @return url
    **/
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    public SubscriptionRequestBody secret(String secret) {
        this.setSecret(secret);
        return this;
    }

    /**
    * A secret to be sent in a header “X-Max-Bot-Api-Secret” in every webhook request, 5-256 characters. Only characters A-Z, a-z, 0-9, _ and - are allowed. The header is useful to ensure that the request comes from a webhook set by you.
    * @return secret
    **/
    @JsonProperty("secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public SubscriptionRequestBody updateTypes(Set<String> updateTypes) {
        this.setUpdateTypes(updateTypes);
        return this;
    }

    /**
    * List of update types your bot want to receive. See &#x60;Update&#x60; object for a complete list of types
    * @return updateTypes
    **/
    @JsonProperty("update_types")
    public Set<String> getUpdateTypes() {
        return updateTypes;
    }

    public void setUpdateTypes(Set<String> updateTypes) {
        this.updateTypes = updateTypes;
    }

    public SubscriptionRequestBody version(String version) {
        this.setVersion(version);
        return this;
    }

    /**
    * Version of API. Affects model representation
    * @return version
    **/
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        SubscriptionRequestBody other = (SubscriptionRequestBody) o;
        return Objects.equals(this.url, other.url) &&
            Objects.equals(this.secret, other.secret) &&
            Objects.equals(this.updateTypes, other.updateTypes) &&
            Objects.equals(this.version, other.version);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (updateTypes != null ? updateTypes.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubscriptionRequestBody{"
            + " url='" + url + '\''
            + " secret='" + secret + '\''
            + " updateTypes='" + updateTypes + '\''
            + " version='" + version + '\''
            + '}';
    }
}
