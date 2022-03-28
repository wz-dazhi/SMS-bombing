package com.github.bombing.strategy;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.execute.strategy
 * @className: JinRongHaoStrategy
 * @description: 金融号
 * @date: 2022/3/15
 * @version: 1.0
 */
public class JinRongHaoStrategy extends AbstractBombingStrategy<Integer> {

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public Strategy strategy() {
        return Strategy.JIN_RONG_HAO;
    }

    @Override
    public URL url() throws MalformedURLException {
        return new URL(String.format(strategy().getUrl(), phone));
    }

    @Override
    protected void doResp(Integer res) {
        logger.info(">>> " + strategy().getDesc() + " 返回内容为: " + res);
    }

}
