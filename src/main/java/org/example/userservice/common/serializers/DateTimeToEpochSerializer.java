package org.example.userservice.common.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serializer to convert {@link LocalDateTime} to epoch timestamp.
 */
public class DateTimeToEpochSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(
            final LocalDateTime localDateTime,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeString(String.valueOf(localDateTime.toEpochSecond(ZoneOffset.UTC)));
    }

}
