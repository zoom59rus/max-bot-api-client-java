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

import org.junit.jupiter.api.Test;
import ru.max.botapi.model.SimpleQueryResult;
import ru.max.botapi.model.SubscriptionRequestBody;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SubscribeQueryTest extends UnitTestBase {
    
    @Test
    public void subscribeTest() throws Exception {
        SubscriptionRequestBody subscriptionRequestBody = new SubscriptionRequestBody("https://someurl.com");
        subscriptionRequestBody.setUpdateTypes(Collections.singleton("message_created"));
        SimpleQueryResult response = api.subscribe(subscriptionRequestBody).execute();
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void shouldThrowException() throws Exception {
        api.subscribe(null).execute();
    }
}
