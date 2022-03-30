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

    /**
     * 有讲
     */
    YO_JIANG("有讲", "https://www.yojiang.cn/api/user/send_verify_code?phone=%s", "GET"),

    /**
     * 51book
     */
    BOOK_51("51book", "http://caigou.51book.com/caigou/dwr/call/plaincall/UserCheck.sendMsgxxx.dwr", "POST"),

    /**
     * 手机中国
     */
    CNMO("手机中国", "http://passport.cnmo.com/index.php?c=Member_Ajax_Register&m=SendMessageCode&Jsoncallback=jQuery18307588430132219965_1648624388005&mobile=%s&type=1&_=1648624591579", "GET"),

    /**
     * 粉笔
     */
    FENBI("粉笔", "https://tiku.fenbi.com/api/users/phone/verification?kav=12&app=web&client_context_id=569d4467893d0e492d8f055dcff47d53", "POST"),
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
