package com.demo.spider;

import com.demo.model.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProxyCrawl implements PageProcessor {

    @Autowired
    private ProxyPipeline proxyPipeline;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    public  String url ="http://www.66ip.cn/%d.html";
    public  int i = 1;
    @Override
    public void process(Page page) {
        ArrayList<Proxy> proxys = null;
        Selectable tbody = page.getHtml().xpath("//*[@id=\"main\"]/div/div[1]/table/tbody/tr");
        List<Selectable> tr = tbody.nodes();
        for (Selectable selectable : tr) {
            List<Selectable> nodes = selectable.xpath("td/text()").nodes();
            if (isboolIp(nodes.get(0).toString())) {
                if (proxys == null) {
                    proxys = new ArrayList<>();
                }
                Proxy e = new Proxy();
                e.setIp(nodes.get(0).toString());
                e.setPort(Integer.parseInt(nodes.get(1).toString()));
                e.setArea(nodes.get(2).toString());
                e.setCreatetime(new Date());
                e.setUpdatetime(new Date());
                proxys.add(e);

            }
        }
        if (proxys != null) {

            page.putField("proxys",proxys);
        }
        if (i < 500) {
            i++;

            page.addTargetRequest(String.format(url,i));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public void start() {
        Spider spider = Spider.create(this).addUrl(String.format(url,i))
                .addPipeline(proxyPipeline).thread(1);
        spider.run();
    }

    /** * 判断是否为合法IP * @return the ip */
    public  boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
