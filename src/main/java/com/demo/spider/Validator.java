package com.demo.spider;

import com.alibaba.fastjson.JSONObject;
import com.demo.dao.ProxysRepository;
import com.demo.model.Proxy;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

@Component
public class Validator {

    @Autowired
    private ProxysRepository proxysRepository;
    private ScheduledThreadPoolExecutor mPoolExecutor;


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
        proxy.setValided(true);
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
        String url = isHttp ? Config.VALIDATOR_HTTP_URL : Config.VALIDATOR_HTTPS_URL;

        java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ips, port));

        //设置代理,需要替换
        HttpURLConnection conn = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection(proxy);
            conn.setConnectTimeout(Config.TIMEOUT);
            conn.setReadTimeout(Config.TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = conn.getResponseCode();

            System.out.println("响应：" + responseCode+":");
            if (responseCode >= 200 && responseCode < 300) {
                String str = IOUtils.toString(conn.getInputStream(), "utf-8");
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
            conn.getInputStream().close();
        } catch (Exception e) {
            System.out.println("一场了");
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
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
        Iterable<Proxy> all = proxysRepository.findAll();
        if (all != null) {
            for(Proxy proxy : all) {
                proxy.setValided(false);
            }
            proxysRepository.save(all);
        }
        mPoolExecutor = new ScheduledThreadPoolExecutor(10);
        for (int j = 0; j < 10; j++) {
            mPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println("validator: -----start: " + Thread.currentThread().getName());
                    for (; ; ) {
                        Proxy proxy = proxysRepository.findProxyByValidedIsFalse();
                        if (proxy != null) {
                            System.out.println("validator: -----" + proxy.getId() + "-------" + new Date().toLocaleString());
                            System.out.println(proxy + "\n");
                            checkHttpProxy(proxy);
                        } else {
                            break;
                        }
                    }
                    System.out.println("validator: -----stop: "+Thread.currentThread().getName());
                }
            });
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        proxy.setIp("218.89.14.142");
        proxy.setPort(8060);
        new Validator().checkHttpProxy(proxy);
    }
}
