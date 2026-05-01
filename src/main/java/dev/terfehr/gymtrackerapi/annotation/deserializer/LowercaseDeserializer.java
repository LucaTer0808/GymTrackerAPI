package dev.terfehr.gymtrackerapi.annotation.deserializer;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class LowercaseDeserializer extends StdDeserializer<String> {
    public LowercaseDeserializer() { super(String.class); }

    @Override
    public String deserialize(JsonParser p, DeserializationContext context) {
        return p.getString().toLowerCase();
    }
}
