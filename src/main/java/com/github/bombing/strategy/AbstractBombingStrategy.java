package com.github.bombing.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.bombing.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @projectName: SMS-bombing
 * @package: com.github.bombing
 * @className: AbstractBombingStrategy
 * @description:
 * @date: 2022/3/15
 * @version: 1.0
 */
public abstract class AbstractBombingStrategy<RESP> implements BombingStrategy {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    private static final BiFunction<Strategy, HttpURLConnection, Supplier<String>> LOG_RESPONSE_SUPPLIER = (strategy, connection) -> () -> {
        try {
            return String.format(">>> %s 请求成功, 返回结果: %s", strategy.getDesc(), connection.getResponseMessage());
        } catch (IOException e) {
            return String.format(">>> %s 请求成功, 获取响应码异常. 异常信息: %s", strategy.getDesc(), e.getMessage());
        }
    };

    protected URLConnection con;

    protected String phone = Config.getInstance().getPhone();

    protected final Map<String, String> HEADERS = new HashMap<>();

    public abstract Strategy strategy();

    protected URL url() throws MalformedURLException {
        return new URL(String.format(strategy().getUrl(), phone));
    }

    protected void before() {
    }

    protected String body() {
        return null;
    }

    protected final URLConnection connection() throws IOException {
        if (this.con != null) {
            return this.con;
        }
        Strategy strategy = strategy();
        URLConnection con = url().openConnection();
        if (con instanceof HttpURLConnection c) {
            c.setRequestMethod(strategy.getMethod());
        }
        HEADERS.forEach(con::setRequestProperty);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.connect();
        this.con = con;
        return this.con;
    }

    @Override
    public void doExec() {
        Strategy strategy = strategy();
        try {
            this.before();
            URLConnection connection = connection();
            String body = body();
            if (null != body && body.length() != 0) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {
                    writer.write(body);
                    writer.flush();
                }
            }

            logger.info(">>> 准备请求: " + strategy.getDesc() + ", 请求地址: " + url().toString());
            if (connection instanceof HttpURLConnection c) {
                int responseCode = c.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String res = readResponse(inputStream);
                    logger.info(LOG_RESPONSE_SUPPLIER.apply(strategy, c));
                    String contentType = c.getHeaderField(CONTENT_TYPE);
                    this.doResp(contentType != null && contentType.contains("json") ? this.resp(res) : (RESP) String.valueOf(res));
                } else {
                    logger.warning(() -> String.format(">>> %s 请求失败, 状态码: %s", strategy.getDesc(), responseCode));
                }

                c.disconnect();
            }

            this.con = null;
        } catch (IOException e) {
            Supplier<String> msg = () -> String.format(">>> 请求异常, 请求网站: %s, 请求方式: %s, url: %s, 异常信息: %s", strategy.getDesc(), strategy.getUrl(), strategy.getMethod(), e.getMessage());
            logger.log(Level.WARNING, msg);
        }
    }

    protected String readResponse(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining());
    }

    protected RESP resp(String res) {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actType = parameterizedType.getActualTypeArguments()[0];
        try {
            return (RESP) OBJECT_MAPPER.readValue(res, (Class<?>) actType);
        } catch (JsonProcessingException e) {
            logger.warning(() -> String.format(">>> 序列化响应结果发生异常. 返回内容: %s, 序列化类型: %s, 异常信息: %s", res, actType.getTypeName(), e.getMessage()));
            return null;
        }
    }

    protected abstract void doResp(RESP res);

}
