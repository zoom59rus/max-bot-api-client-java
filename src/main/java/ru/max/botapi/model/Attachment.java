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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Generic schema representing message attachment
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = Attachment.class, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = PhotoAttachment.class, name = Attachment.IMAGE),
  @JsonSubTypes.Type(value = VideoAttachment.class, name = Attachment.VIDEO),
  @JsonSubTypes.Type(value = AudioAttachment.class, name = Attachment.AUDIO),
  @JsonSubTypes.Type(value = FileAttachment.class, name = Attachment.FILE),
  @JsonSubTypes.Type(value = StickerAttachment.class, name = Attachment.STICKER),
  @JsonSubTypes.Type(value = ContactAttachment.class, name = Attachment.CONTACT),
  @JsonSubTypes.Type(value = InlineKeyboardAttachment.class, name = Attachment.INLINE_KEYBOARD),
  @JsonSubTypes.Type(value = ReplyKeyboardAttachment.class, name = Attachment.REPLY_KEYBOARD),
  @JsonSubTypes.Type(value = ShareAttachment.class, name = Attachment.SHARE),
  @JsonSubTypes.Type(value = LocationAttachment.class, name = Attachment.LOCATION),
  @JsonSubTypes.Type(value = DataAttachment.class, name = Attachment.DATA),
})
@KnownInstance(ofClass = Attachment.class, discriminator = "type")
public class Attachment implements MaxSerializable {
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String FILE = "file";
    public static final String STICKER = "sticker";
    public static final String CONTACT = "contact";
    public static final String INLINE_KEYBOARD = "inline_keyboard";
    public static final String REPLY_KEYBOARD = "reply_keyboard";
    public static final String SHARE = "share";
    public static final String LOCATION = "location";
    public static final String DATA = "data";
    public static final Set<String> TYPES = new HashSet<>(Arrays.asList(
        IMAGE, 
        VIDEO, 
        AUDIO, 
        FILE, 
        STICKER, 
        CONTACT, 
        INLINE_KEYBOARD, 
        REPLY_KEYBOARD, 
        SHARE, 
        LOCATION, 
        DATA
    ));




    public void visit(Visitor visitor) {
        visitor.visitDefault(this);
    }

    public <T> T map(Mapper<T> mapper) {
        return mapper.mapDefault(this);
    }

    @JsonProperty("type")
    public String getType() {
        throw new UnsupportedOperationException("Model has no concrete type.");
    }

    @Override
    public String toString() {
        return "Attachment{"
            + '}';
    }

    public interface Visitor {
        void visit(PhotoAttachment model);
        void visit(VideoAttachment model);
        void visit(AudioAttachment model);
        void visit(FileAttachment model);
        void visit(StickerAttachment model);
        void visit(ContactAttachment model);
        void visit(InlineKeyboardAttachment model);
        void visit(ReplyKeyboardAttachment model);
        void visit(ShareAttachment model);
        void visit(LocationAttachment model);
        void visit(DataAttachment model);
        void visitDefault(Attachment model);
    }

    public interface Mapper<T> {
        T map(PhotoAttachment model);
        T map(VideoAttachment model);
        T map(AudioAttachment model);
        T map(FileAttachment model);
        T map(StickerAttachment model);
        T map(ContactAttachment model);
        T map(InlineKeyboardAttachment model);
        T map(ReplyKeyboardAttachment model);
        T map(ShareAttachment model);
        T map(LocationAttachment model);
        T map(DataAttachment model);
        T mapDefault(Attachment model);
    }
}
