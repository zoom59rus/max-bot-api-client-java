package ru.max.botapi.model;






import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Tag("UnitTest")
public class UploadTypeTest {
    @Test
    public void shouldConvertToStringAndBackAgain() {
        assertThat(UploadType.create(UploadType.FILE.getValue()), is(UploadType.FILE));
    }
}