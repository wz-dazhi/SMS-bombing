package com.github.bombing.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: BombingStrategy
 * @description:
 * @date: 2022/3/15
 * @version: 1.0
 */
public interface BombingStrategy {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default boolean isAvailable() {
        return true;
    }

    void doExec();
}
