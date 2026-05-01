package dev.terfehr.gymtrackerapi.annotation.deserializer;

import com.nimbusds.jose.shaded.gson.JsonParseException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class TrimDeserializer extends StdDeserializer<String> {
    public TrimDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws JsonParseException {
        String value = parser.getValueAsString();
        return (value != null) ?  value.trim() : null;
    }
}
