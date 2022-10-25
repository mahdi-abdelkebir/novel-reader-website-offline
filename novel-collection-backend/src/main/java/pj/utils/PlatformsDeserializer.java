//package pj.utils;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//
//import pj.models.Platform;
//
//import java.io.IOException;
//
//public class PlatformsDeserializer extends StdDeserializer<Platform> {
//
//    public PlatformsDeserializer() {
//        super(Platform.class);
//    }
//
//    protected PlatformsDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    @Override
//    public Platform deserialize(JsonParser p, DesrializationContext ctxt) throws IOException {
//        Platform platform = new Platform();
//        JsonNode node = p.getCodec().readTree(p);
//        platform.setId(node.get("platform").get("id").asLong());
//        platform.setSlug(node.get("platform").get("name").asText());
//        return platform;
//    }
//}e
