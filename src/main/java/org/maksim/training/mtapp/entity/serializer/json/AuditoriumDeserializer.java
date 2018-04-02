package org.maksim.training.mtapp.entity.serializer.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AuditoriumDeserializer extends JsonDeserializer<Auditorium> {
    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumDeserializer(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @Override
    @SneakyThrows
    public Auditorium deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode mainNode = parser.getCodec().readTree(parser);
        JsonNode nodeName = mainNode.get("name");
        return nodeName != null ? auditoriumService.getByName(nodeName.asText()) : null;
    }
}