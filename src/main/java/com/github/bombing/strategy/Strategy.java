package com.github.bombing.strategy;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: Strategy
 * @description:
 * @date: 2022/3/15
 * @version: 1.0
 */
public enum Strategy {

    /**
     * 金融号
     */
    JIN_RONG_HAO("金融号", "https://jrh.financeun.com/Login/sendMessageCode3.html?mobile=%s&mbid=197858&check=3", "GET"),

    ;

    private final String desc;
    private final String url;
    private final String method;

    Strategy(String desc, String url, String method) {
        this.desc = desc;
        this.url = url;
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
}
