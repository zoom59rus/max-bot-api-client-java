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
 * LocationAttachment
 */
public class LocationAttachment extends Attachment implements MaxSerializable {

    @NotNull
    private final @Valid Double latitude;
    @NotNull
    private final @Valid Double longitude;

    @JsonCreator
    public LocationAttachment(@JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude) { 
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T map(Mapper<T> mapper) {
        return mapper.map(this);
    }

    /**
    * @return latitude
    **/
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    /**
    * @return longitude
    **/
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return Attachment.LOCATION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        LocationAttachment other = (LocationAttachment) o;
        return Objects.equals(this.latitude, other.latitude) &&
            Objects.equals(this.longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocationAttachment{"+ super.toString()
            + " latitude='" + latitude + '\''
            + " longitude='" + longitude + '\''
            + '}';
    }
}
