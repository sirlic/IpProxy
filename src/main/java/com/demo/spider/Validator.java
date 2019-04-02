package com.demo.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.dao.ProxysRepository;
import com.demo.model.Proxy;
import org.apache.http.HttpConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Validator {

    private AtomicLong i;

    @Autowired
    private ProxysRepository proxysRepository;

    public void checkHttpProxy(Proxy proxy) {

        int types = -1;
        long speed = -1;
        int http = -1;

        Value value;
        value = _checkProxy(proxy.getIp(), proxy.getPort(), true);
        if (value != null) {
            types = value.types;
            speed = value.speed;
            http = value.http;
        }
        value = _checkProxy(proxy.getIp(), proxy.getPort(), true);
        if (value != null) {
            types = value.types;
            speed = value.speed;
            if (http == -1) {
                http = Config.HTTPS;
            } else {
                http = Config.HTTP_HTTPS;
            }
        }
        if (http != -1) {
            proxysRepository.updateById(proxy.getId(), http, types, speed, new Date());
            proxysRepository.updateScoreIncByid(proxy.getId());
        } else {
            proxysRepository.updateScoreDecByid(proxy.getId());
            if (proxy.getScore() < -5) {
                proxysRepository.delete(proxy.getId());
            }
        }
    }

    private Value _checkProxy(String ips, int port, boolean isHttp) {
        Value value = new Value();
        value.http = isHttp ? Config.HTTP : Config.HTTPS;

        long start = System.currentTimeMillis();
        //设置代理IP、端口、协议（请分别替换）
        HttpHost proxy = new HttpHost(ips, port);

        //把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(Config.TIMEOUT)
                .build();

        //实例化CloseableHttpClient对象
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        String url = isHttp ? Config.VALIDATOR_HTTP_URL : Config.VALIDATOR_HTTPS_URL;
        //访问目标地址
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse httpResp = null;
        try {
            //请求返回
            httpResp = httpclient.execute(httpGet);
            int statusCode = httpResp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                value.speed = System.currentTimeMillis() - start;
                String str = EntityUtils.toString(httpResp.getEntity());
                System.out.println(str);
                JSONObject jsonObject = JSONObject.parseObject(str);
                JSONObject header = jsonObject.getJSONObject("headers");
                String ip = jsonObject.getString("origin");
                String proxy_conn = header.getString("Proxy-Connection'");
                if (ip != null && ip.contains(",")) {
                    value.types = 2;
                } else if (proxy_conn != null) {
                    value.types = 1;
                } else {
                    value.types = 0;
                }
                System.out.println(value);
                return value;

            }
        } catch (Exception e) {

        } finally {
            if (httpResp != null) {
                try {
                    httpResp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static class Value {
        public int types = -1;
        public long speed = -1;
        public int http = -1;

        @Override
        public String toString() {
            return "Value{" +
                    "types=" + types +
                    ", speed=" + speed +
                    ", http=" + http +
                    '}';
        }
    }

    public void start() {
        i = new AtomicLong();
        i.set(0);
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(10);
        for (int j = 0; j < 10; j++) {
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println("validator: -----start: "+Thread.currentThread().getName());
                    for (; ; ) {
                        long andIncrement = i.getAndIncrement();
                        Proxy proxy = proxysRepository.findOne(andIncrement);
                        if (proxy != null) {
                            System.out.println("validator: -----" + i.get() + "-------");
                            System.out.println(proxy + "\n");
                            checkHttpProxy(proxy);
                        } else {
                            break;
                        }
                    }
                    System.out.println("validator: -----stop: "+Thread.currentThread().getName());
                }
            });

        }

    }

    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        proxy.setIp("113.200.159.155");
        proxy.setPort(9999);
        new Validator().checkHttpProxy(proxy);
    }
}
