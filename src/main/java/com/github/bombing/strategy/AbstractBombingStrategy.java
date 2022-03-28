package com.github.bombing.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bombing.config.Config;

import javax.net.ssl.HttpsURLConnection;
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
import java.nio.charset.StandardCharsets;
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

    protected HttpsURLConnection con;

    protected String phone = Config.getInstance().getPhone();

    public abstract Strategy strategy();

    public abstract URL url() throws MalformedURLException;

    public String params() {
        return null;
    }

    protected final HttpsURLConnection connection() throws IOException {
        if (this.con != null) {
            return this.con;
        }
        Strategy strategy = strategy();
        this.con = (HttpsURLConnection) url().openConnection();
        this.con.setRequestMethod(strategy.getMethod());
        this.con.setDoOutput(true);
        this.con.setDoInput(true);
        this.con.setUseCaches(false);
        this.con.connect();
        return this.con;
    }

    @Override
    public void doExec() {
        Strategy strategy = strategy();
        try {
            HttpURLConnection connection = connection();
            if (null != params() && params().length() != 0) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(params());
                writer.close();
            }

            logger.info(">>> 准备请求: " + strategy.getDesc() + ", 请求地址: " + url().toString());
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String res = readResponse(inputStream);
                logger.info(() -> {
                    try {
                        return String.format(">>> %s 请求成功, 返回结果: %s", strategy.getDesc(), connection.getResponseMessage());
                    } catch (IOException e) {
                        return String.format(">>> %s 请求成功, 获取响应码异常. 异常信息: %s", strategy.getDesc(), e.getMessage());
                    }
                });
                this.doResp(this.resp(res));
            } else {
                logger.warning(() -> String.format(">>> %s 请求失败, 状态码: %s", strategy.getDesc(), responseCode));
            }
        } catch (IOException e) {
            Supplier<String> msg = () -> String.format(">>> 请求异常, 请求网站: %s, 请求方式: %s, url: %s, 异常信息: %s", strategy.getDesc(), strategy.getUrl(), strategy.getMethod(), e);
            logger.log(Level.WARNING, msg);
        }
    }

    protected String readResponse(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining());
    }

    protected RESP resp(String res) {
        ObjectMapper objectMapper = new ObjectMapper();
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actType = parameterizedType.getActualTypeArguments()[0];
        try {
            return (RESP) objectMapper.readValue(res, (Class<?>) actType);
        } catch (JsonProcessingException e) {
            logger.warning(() -> String.format(">>> 序列化响应结果发生异常. 返回内容: %s, 序列化类型: %s, 异常信息: %s", res, actType.getTypeName(), e.getMessage()));
            return null;
        }
    }

    protected abstract void doResp(RESP res);

}
