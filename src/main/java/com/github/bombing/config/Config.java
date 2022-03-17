package com.github.bombing.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.config
 * @className: Config
 * @description:
 * @date: 2022/3/16
 * @version: 1.0
 */
public class Config {
    private static final Logger log = Logger.getLogger(Config.class.getName());
    private static final Map<String, Object> OPTION_MAP = new HashMap<>();
    private static final List<String> NON_OPTION_LIST = new ArrayList<>();
    private static Config CONFIG;

    private String phone;

    private Config() {
    }

    public static Config getInstance() {
        if (CONFIG == null) {
            throw new IllegalArgumentException("Init config failed. ");
        }
        return CONFIG;
    }

    public static synchronized Config initConfig(String[] args) {
        if (CONFIG != null) {
            return CONFIG;
        }

        if (!verifyArgs(args)) {
            return null;
        }
        log.info(() -> String.format(">>> 准备开始轰炸. 输入参数为: \n%s", String.join("\n", args)));
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String optionText = arg.substring(2);
                String optionName;
                String optionValue = null;
                if (optionText.contains("=")) {
                    optionName = optionText.substring(0, optionText.indexOf('='));
                    optionValue = optionText.substring(optionText.indexOf('=') + 1);
                } else {
                    optionName = optionText;
                }
                if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
                    throw new IllegalArgumentException("Invalid argument syntax: " + arg);
                }
                OPTION_MAP.put(optionName, optionValue);
            } else {
                NON_OPTION_LIST.add(arg);
            }
        }
        Class<Config> clazz = Config.class;
        try {
            Constructor<Config> constructor = clazz.getDeclaredConstructor();
            CONFIG = constructor.newInstance();
            OPTION_MAP.forEach((field, value) -> {
                try {
                    Field f = clazz.getDeclaredField(field);
                    f.setAccessible(true);
                    f.set(CONFIG, value);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            });
            CONFIG.verifyFields();
            return CONFIG;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.warning(">>> 初始化参数失败." + e);
            throw new IllegalArgumentException("Init argument failed. " + e.getMessage());
        }
    }

    private static boolean verifyArgs(String[] args) {
        if (args == null || args.length == 0) {
            log.warning("参数为空!");
            return false;
        }

        return true;
    }

    private void verifyFields() {
        if (this.phone == null || this.phone.length() == 0) {
            throw new IllegalArgumentException("Invalid phone failed.");
        }
    }

    public String getPhone() {
        return phone;
    }
}
