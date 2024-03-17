//package com.hps.osvscanning.schedule.config;
//
//import com.fasterxml.jackson.core.JsonToken;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.json.JsonObjectReader;
//import org.springframework.core.io.Resource;
//import org.springframework.lang.Nullable;
//import org.springframework.util.Assert;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//public class CustomJsonObjectReader<T> implements JsonObjectReader<T> {
//    private final Class<T> itemType;
//    private ObjectMapper mapper;
//    private String contentStr;
//
//    public CustomJsonObjectReader(ObjectMapper mapper, Class<T> itemType) {
//        this.mapper = mapper;
//        this.itemType = itemType;
//    }
//
//    public void setMapper(ObjectMapper mapper) {
//        Assert.notNull(mapper, "The mapper must not be null");
//        this.mapper = mapper;
//    }
//
//    public void open(Resource resource) throws Exception {
//        Assert.notNull(resource, "The resource must not be null");
//        this.contentStr = resource.getContentAsString(Charset.defaultCharset());
//        Assert.state(this.contentStr.startsWith(JsonToken.START_OBJECT.asString()), "The Json input stream must start with an object of Json objects");
//    }
//
//    @Nullable
//    public T read() throws Exception {
//        try {
//            return this.mapper.readValue(this.contentStr, this.itemType);
//        } catch (IOException var2) {
//            throw new ParseException("Unable to read next JSON object", var2);
//        }
//    }
//
//    public void close() throws Exception {
//
//    }
//}
