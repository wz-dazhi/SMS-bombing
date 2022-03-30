package com.github.bombing.strategy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: BombingStrategy
 * @description:
 * @date: 2022/3/15
 * @version: 1.0
 */
public interface BombingStrategy {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper() {{
        this.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }};

    String CONTENT_TYPE = "Content-Type";
    String COOKIE = "Cookie";

    default boolean isAvailable() {
        return true;
    }

    default String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    void doExec();
}
