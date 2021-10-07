package com.yikekong.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtil{
    /**
     * 反序列化
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T getByJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //反序列化"2020-01-22T11:11:11"字符串为LocalDateTime格式的配置
        mapper.registerModule(new JavaTimeModule());

        return mapper.readValue(json, clazz);
    }

    /**
     * 从json字符串中根据nodeName获取值
     * @param nodeName
     * @param json
     * @return
     * @throws IOException
     */
    public static String getValueByNodeName(String nodeName, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        return jsonNode.findPath(nodeName).asText();
    }

    /**
     * 序列化
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String serialize(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //反序列化"2020-01-22T11:11:11"字符串为LocalDateTime格式的配置
        mapper.registerModule(new JavaTimeModule());

        return mapper.writeValueAsString(object);
    }
}
