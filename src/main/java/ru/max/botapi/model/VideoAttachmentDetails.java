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
 * VideoAttachmentDetails
 */
public class VideoAttachmentDetails implements MaxSerializable {

    @NotNull
    private final @Valid String token;
    @Nullable
    private @Valid VideoUrls urls;
    @Nullable
    private @Valid PhotoAttachmentPayload thumbnail;
    @NotNull
    private final @Valid Integer width;
    @NotNull
    private final @Valid Integer height;
    @NotNull
    private final @Valid Integer duration;

    @JsonCreator
    public VideoAttachmentDetails(@JsonProperty("token") String token, @JsonProperty("width") Integer width, @JsonProperty("height") Integer height, @JsonProperty("duration") Integer duration) { 
        this.token = token;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    /**
    * Video attachment token
    * @return token
    **/
    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public VideoAttachmentDetails urls(@Nullable VideoUrls urls) {
        this.setUrls(urls);
        return this;
    }

    /**
    * URLs to download or play video. Can be null if video is unavailable
    * @return urls
    **/
    @Nullable
    @JsonProperty("urls")
    public VideoUrls getUrls() {
        return urls;
    }

    public void setUrls(@Nullable VideoUrls urls) {
        this.urls = urls;
    }

    public VideoAttachmentDetails thumbnail(@Nullable PhotoAttachmentPayload thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    /**
    * Video thumbnail
    * @return thumbnail
    **/
    @Nullable
    @JsonProperty("thumbnail")
    public PhotoAttachmentPayload getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@Nullable PhotoAttachmentPayload thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
    * Video width
    * @return width
    **/
    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    /**
    * Video height
    * @return height
    **/
    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    /**
    * Video duration in seconds
    * @return duration
    **/
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        VideoAttachmentDetails other = (VideoAttachmentDetails) o;
        return Objects.equals(this.token, other.token) &&
            Objects.equals(this.urls, other.urls) &&
            Objects.equals(this.thumbnail, other.thumbnail) &&
            Objects.equals(this.width, other.width) &&
            Objects.equals(this.height, other.height) &&
            Objects.equals(this.duration, other.duration);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (urls != null ? urls.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VideoAttachmentDetails{"
            + " token='" + token + '\''
            + " urls='" + urls + '\''
            + " thumbnail='" + thumbnail + '\''
            + " width='" + width + '\''
            + " height='" + height + '\''
            + " duration='" + duration + '\''
            + '}';
    }
}
