package vip.geekclub.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongrui
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    /**
     * -- GETTER --
     *  获取ObjectMapper
     */
    @Getter
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        objectMapper.registerModule(javaTimeModule);

        //设置返回null转为 空字符串""
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<>() {
            @Override
            public void serialize(Object paramT, JsonGenerator paramJsonGenerator,
                                  SerializerProvider paramSerializerProvider) throws IOException {
                paramJsonGenerator.writeString("");
            }
        });
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return toJsonString(object, false);
    }

    public static String toJsonString(Object object, boolean isPretty) throws JsonProcessingException {
        return toJsonString(object, null, null, null, isPretty);
    }

    public static String toJsonString(Object object, String[] include, String[] filter, String filterId) throws JsonProcessingException {
        return toJsonString(object, include, filter, filterId, false);
    }

    public static String toJsonString(Object object, String[] include, String[] filter, String filterId, boolean isPretty) throws JsonProcessingException {

        if (null != filterId && !filterId.isEmpty()) {
            if (null != include && include.length > 0) {
                objectMapper.setFilterProvider((new SimpleFilterProvider()).addFilter(filterId, SimpleBeanPropertyFilter.filterOutAllExcept(include)));
            } else if (null != filter && filter.length > 0) {
                objectMapper.setFilterProvider((new SimpleFilterProvider()).addFilter(filterId, SimpleBeanPropertyFilter.serializeAllExcept(filter)));
            } else {
                objectMapper.setFilterProvider((new SimpleFilterProvider()).addFilter(filterId, SimpleBeanPropertyFilter.serializeAllExcept("")));
            }
        }

        String json;
        if (isPretty) {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            json = objectMapper.writeValueAsString(object);
        }

        return json;
    }

    public static <T> T parseJsonToCertainClass(String json, Class<T> clz) throws IOException {
        return objectMapper.readValue(json, clz);
    }

    public static <T> T parseJsonToCertainClass123(String json, Class<T> clz) throws IOException {
        return objectMapper.readValue(json, clz);
    }

    public static <T> T jsonToObject(JsonNode jsonNode, Class<T> T) throws JsonProcessingException {
        return objectMapper.treeToValue(jsonNode, T);
    }

    /**
     * Json 转成List集合
     */
    public static List<?> jsonToList(JsonNode jsonNode, Class<?> T) throws IOException {
        return jsonToList(jsonNode.toString(), T);
    }

    /**
     * Json 转成List集合
     */
    static List<?> jsonToList(String json, Class<?> T) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, T);
        return objectMapper.readValue(json, javaType);
    }
}