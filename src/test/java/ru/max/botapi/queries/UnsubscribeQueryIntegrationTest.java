package ru.max.botapi.queries;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.SimpleQueryResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;


public class UnsubscribeQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldReturnUnsuccessfulResultOnNonExistingSubscription() throws Exception {
        String url = "https://" + randomText(32) + ".com";
        SimpleQueryResult result = new UnsubscribeQuery(client, url).execute();
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getMessage(), not(isEmptyOrNullString()));
    }
}