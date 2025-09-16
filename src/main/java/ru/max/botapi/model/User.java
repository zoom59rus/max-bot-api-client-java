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
 * User
 */
public class User implements MaxSerializable {

    @NotNull
    private final @Valid Long userId;
    @Nullable
    private @Valid String name;
    @NotNull
    private final @Valid String firstName;
    @Nullable
    private final @Valid String lastName;
    @Nullable
    private final @Valid String username;
    @NotNull
    private final @Valid Boolean isBot;
    @NotNull
    private final @Valid Long lastActivityTime;

    @JsonCreator
    public User(@JsonProperty("user_id") Long userId, @JsonProperty("first_name") String firstName, @Nullable @JsonProperty("last_name") String lastName, @Nullable @JsonProperty("username") String username, @JsonProperty("is_bot") Boolean isBot, @JsonProperty("last_activity_time") Long lastActivityTime) { 
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.isBot = isBot;
        this.lastActivityTime = lastActivityTime;
    }

    /**
    * Users identifier
    * @return userId
    **/
    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    public User name(@Nullable String name) {
        this.setName(name);
        return this;
    }

    /**
    * Users visible name
    * @return name
    **/
    @Nullable
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
    * Users first name
    * @return firstName
    **/
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    /**
    * Users last name
    * @return lastName
    **/
    @Nullable
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    /**
    * Unique public user name. Can be &#x60;null&#x60; if user is not accessible or it is not set
    * @return username
    **/
    @Nullable
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
    * &#x60;true&#x60; if user is bot
    * @return isBot
    **/
    @JsonProperty("is_bot")
    public Boolean isBot() {
        return isBot;
    }

    /**
    * Time of last user activity in Max (Unix timestamp in milliseconds). Can be outdated if user disabled its \&quot;online\&quot; status in settings
    * @return lastActivityTime
    **/
    @JsonProperty("last_activity_time")
    public Long getLastActivityTime() {
        return lastActivityTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        User other = (User) o;
        return Objects.equals(this.userId, other.userId) &&
            Objects.equals(this.name, other.name) &&
            Objects.equals(this.firstName, other.firstName) &&
            Objects.equals(this.lastName, other.lastName) &&
            Objects.equals(this.username, other.username) &&
            Objects.equals(this.isBot, other.isBot) &&
            Objects.equals(this.lastActivityTime, other.lastActivityTime);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isBot != null ? isBot.hashCode() : 0);
        result = 31 * result + (lastActivityTime != null ? lastActivityTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{"
            + " userId='" + userId + '\''
            + " name='" + name + '\''
            + " firstName='" + firstName + '\''
            + " lastName='" + lastName + '\''
            + " username='" + username + '\''
            + " isBot='" + isBot + '\''
            + " lastActivityTime='" + lastActivityTime + '\''
            + '}';
    }
}
