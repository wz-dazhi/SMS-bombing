package com.github.bombing.strategy;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: BombingStrategy
 * @description:
 * @date: 2022/3/15
 * @version: 1.0
 */
public interface BombingStrategy {

    default boolean isAvailable() {
        return true;
    }

    void doExec();
}
