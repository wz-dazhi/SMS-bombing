package com.github.bombing;

import com.github.bombing.strategy.BombingStrategy;
import com.github.bombing.strategy.JinRongHaoStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: BombingExecutor
 * @description: 轰炸执行器
 * @version: 1.0
 */
public class BombingExecutor {
    private final List<BombingStrategy> bombings;

    public BombingExecutor() {
        this.bombings = new ArrayList<>();
        this.initBombingStrategy();
    }

    private void initBombingStrategy() {
        bombings.add(new JinRongHaoStrategy());
    }

    public void exec() {
        bombings.stream()
                .filter(BombingStrategy::isAvailable)
                .forEach(BombingStrategy::doExec);
    }
}
