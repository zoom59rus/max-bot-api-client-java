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

package ru.max.botapi.queries;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ru.max.botapi.exceptions.RequiredParameterMissingException;
import ru.max.botapi.model.BotCommand;
import ru.max.botapi.model.BotInfo;
import ru.max.botapi.model.BotPatch;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static spark.Spark.patch;

public class EditMyInfoQueryTest extends UnitTestBase {
    
    @Test
    public void editMyInfoTest() throws Exception {
        patch("/me", (req, resp) -> {
            BotPatch patch = serializer.deserialize(req.body(), BotPatch.class);
            BotInfo botInfo = new BotInfo(me.getUserId(), patch.getName(), null, "botusername", true,
                    System.currentTimeMillis());
            botInfo.commands(patch.getCommands());
            botInfo.description(patch.getDescription());
            botInfo.avatarUrl(patch.getPhoto().getUrl());
            botInfo.fullAvatarUrl(patch.getPhoto().getUrl());
            return botInfo;
        }, this::serialize);

        List<BotCommand> commands = Collections.singletonList(new BotCommand("name").description("description"));
        String botname = "botname";
        String description = "botdescription";
        BotPatch botPatch = new BotPatch()
                .commands(commands)
                .name(botname)
                .description(description)
                .photo(new PhotoAttachmentRequestPayload().url("patchurl"));

        EditMyInfoQuery query = api.editMyInfo(botPatch);
        BotInfo response = query.execute();

        assertThat(response.getCommands(), is(commands));
        assertThat(response.getFirstName(), is(botname));
        assertThat(response.getDescription(), is(description));
    }

    @Test(expected = RequiredParameterMissingException.class)
    public void shouldThrow() throws Exception {
        api.editMyInfo(null).execute();
    }

}
