package com.github.bombing.strategy;

import com.github.bombing.config.Config;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
public abstract class AbstractBombingStrategy implements BombingStrategy {

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
                logger.info(">>> 请求成功. \n返回结果: " + connection.getResponseMessage());
                this.doResp(res);
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

    protected abstract void doResp(String res);

}
