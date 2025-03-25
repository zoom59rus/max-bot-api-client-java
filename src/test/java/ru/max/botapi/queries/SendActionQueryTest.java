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

import org.junit.Test;

import ru.max.botapi.exceptions.RequiredParameterMissingException;
import ru.max.botapi.model.ActionRequestBody;
import ru.max.botapi.model.SenderAction;
import ru.max.botapi.model.SimpleQueryResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SendActionQueryTest extends UnitTestBase {
    
    @Test
    public void sendActionTest() throws Exception {
        ActionRequestBody actionRequestBody = new ActionRequestBody(SenderAction.TYPING_ON);
        Long chatId = 1L;
        SimpleQueryResult response = api.sendAction(actionRequestBody, chatId).execute();
        assertThat(response.isSuccess(), is(true));
    }

    @Test(expected = RequiredParameterMissingException.class)
    public void shouldThrowException() throws Exception {
        api.sendAction(null, 1L).execute();
    }

    @Test(expected = RequiredParameterMissingException.class)
    public void shouldThrowException2() throws Exception {
        api.sendAction(new ActionRequestBody(SenderAction.TYPING_ON), null).execute();
    }
}
