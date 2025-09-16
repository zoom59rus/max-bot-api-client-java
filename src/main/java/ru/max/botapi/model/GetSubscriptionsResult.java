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
 * List of all WebHook subscriptions
 */
public class GetSubscriptionsResult implements MaxSerializable {

    @NotNull
    private final List<@Valid Subscription> subscriptions;

    @JsonCreator
    public GetSubscriptionsResult(@JsonProperty("subscriptions") List<Subscription> subscriptions) { 
        this.subscriptions = subscriptions;
    }

    /**
    * Current subscriptions
    * @return subscriptions
    **/
    @JsonProperty("subscriptions")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        GetSubscriptionsResult other = (GetSubscriptionsResult) o;
        return Objects.equals(this.subscriptions, other.subscriptions);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GetSubscriptionsResult{"
            + " subscriptions='" + subscriptions + '\''
            + '}';
    }
}
