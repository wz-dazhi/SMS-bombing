package com.github.bombing.strategy;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing.execute.strategy
 * @className: YoJiangStrategy
 * @description: 有讲
 * @date: 2022/3/15
 * @version: 1.0
 */
public class YoJiangStrategy extends AbstractBombingStrategy<YoJiangStrategy.Resp> {

    @Override
    public Strategy strategy() {
        return Strategy.YO_JIANG;
    }

    @Override
    protected void before() {
        HEADERS.put(COOKIE, "guest_uuid=" + uuid().substring(0, 24) + "");
    }

    @Override
    protected void doResp(Resp res) {
        if (!res.isSuccess()) {
            logger.info(">>> " + strategy().getDesc() + " 请求失败, 返回内容为: " + res);
        }
    }

    public static class Resp implements Serializable {
        private String message;
        private int code;
        private boolean success;
        private Object data;
        @JsonAlias("encrypt_ver")
        private Object encryptVer;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getEncryptVer() {
            return encryptVer;
        }

        public void setEncryptVer(Object encryptVer) {
            this.encryptVer = encryptVer;
        }

        @Override
        public String toString() {
            return "Resp{" +
                    "message='" + message + '\'' +
                    ", code=" + code +
                    ", success=" + success +
                    ", data=" + data +
                    ", encryptVer=" + encryptVer +
                    '}';
        }
    }
}
