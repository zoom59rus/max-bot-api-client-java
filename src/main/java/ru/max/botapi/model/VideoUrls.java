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
import org.jetbrains.annotations.Nullable;

import javax.validation.Valid;
import java.util.Objects;

/**
 * VideoUrls
 */
public class VideoUrls implements MaxSerializable {

    @Nullable
    private @Valid String mp41080;
    @Nullable
    private @Valid String mp4720;
    @Nullable
    private @Valid String mp4480;
    @Nullable
    private @Valid String mp4360;
    @Nullable
    private @Valid String mp4240;
    @Nullable
    private @Valid String mp4144;
    @Nullable
    private @Valid String hls;

    public VideoUrls mp41080(@Nullable String mp41080) {
        this.setMp41080(mp41080);
        return this;
    }

    /**
    * Video URL in 1080p resolution, if available
    * @return mp41080
    **/
    @Nullable
    @JsonProperty("mp4_1080")
    public String getMp41080() {
        return mp41080;
    }

    public void setMp41080(@Nullable String mp41080) {
        this.mp41080 = mp41080;
    }

    public VideoUrls mp4720(@Nullable String mp4720) {
        this.setMp4720(mp4720);
        return this;
    }

    /**
    * Video URL in 720 resolution, if available
    * @return mp4720
    **/
    @Nullable
    @JsonProperty("mp4_720")
    public String getMp4720() {
        return mp4720;
    }

    public void setMp4720(@Nullable String mp4720) {
        this.mp4720 = mp4720;
    }

    public VideoUrls mp4480(@Nullable String mp4480) {
        this.setMp4480(mp4480);
        return this;
    }

    /**
    * Video URL in 480 resolution, if available
    * @return mp4480
    **/
    @Nullable
    @JsonProperty("mp4_480")
    public String getMp4480() {
        return mp4480;
    }

    public void setMp4480(@Nullable String mp4480) {
        this.mp4480 = mp4480;
    }

    public VideoUrls mp4360(@Nullable String mp4360) {
        this.setMp4360(mp4360);
        return this;
    }

    /**
    * Video URL in 360 resolution, if available
    * @return mp4360
    **/
    @Nullable
    @JsonProperty("mp4_360")
    public String getMp4360() {
        return mp4360;
    }

    public void setMp4360(@Nullable String mp4360) {
        this.mp4360 = mp4360;
    }

    public VideoUrls mp4240(@Nullable String mp4240) {
        this.setMp4240(mp4240);
        return this;
    }

    /**
    * Video URL in 240 resolution, if available
    * @return mp4240
    **/
    @Nullable
    @JsonProperty("mp4_240")
    public String getMp4240() {
        return mp4240;
    }

    public void setMp4240(@Nullable String mp4240) {
        this.mp4240 = mp4240;
    }

    public VideoUrls mp4144(@Nullable String mp4144) {
        this.setMp4144(mp4144);
        return this;
    }

    /**
    * Video URL in 144 resolution, if available
    * @return mp4144
    **/
    @Nullable
    @JsonProperty("mp4_144")
    public String getMp4144() {
        return mp4144;
    }

    public void setMp4144(@Nullable String mp4144) {
        this.mp4144 = mp4144;
    }

    public VideoUrls hls(@Nullable String hls) {
        this.setHls(hls);
        return this;
    }

    /**
    * Live streaming URL, if available
    * @return hls
    **/
    @Nullable
    @JsonProperty("hls")
    public String getHls() {
        return hls;
    }

    public void setHls(@Nullable String hls) {
        this.hls = hls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        VideoUrls other = (VideoUrls) o;
        return Objects.equals(this.mp41080, other.mp41080) &&
            Objects.equals(this.mp4720, other.mp4720) &&
            Objects.equals(this.mp4480, other.mp4480) &&
            Objects.equals(this.mp4360, other.mp4360) &&
            Objects.equals(this.mp4240, other.mp4240) &&
            Objects.equals(this.mp4144, other.mp4144) &&
            Objects.equals(this.hls, other.hls);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (mp41080 != null ? mp41080.hashCode() : 0);
        result = 31 * result + (mp4720 != null ? mp4720.hashCode() : 0);
        result = 31 * result + (mp4480 != null ? mp4480.hashCode() : 0);
        result = 31 * result + (mp4360 != null ? mp4360.hashCode() : 0);
        result = 31 * result + (mp4240 != null ? mp4240.hashCode() : 0);
        result = 31 * result + (mp4144 != null ? mp4144.hashCode() : 0);
        result = 31 * result + (hls != null ? hls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VideoUrls{"
            + " mp41080='" + mp41080 + '\''
            + " mp4720='" + mp4720 + '\''
            + " mp4480='" + mp4480 + '\''
            + " mp4360='" + mp4360 + '\''
            + " mp4240='" + mp4240 + '\''
            + " mp4144='" + mp4144 + '\''
            + " hls='" + hls + '\''
            + '}';
    }
}
