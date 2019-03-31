package com.demo.spider.crawl.xicidaili;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

@Component
public class Proxy_xicidaili_wt extends Proxy_xicidaili_base {

    public String host = "https://www.xicidaili.com/";
    public  String url = host + "wt/%d/";
    public  int i = 1;

    @Override
    public void process(Page page) {
        super.process(page);
        if (i < 500) {
            i++;

            page.addTargetRequest(String.format(url,i));
        }
    }

    @Override
    public boolean isMatch(Page page) {
        return page.getRequest().getUrl().startsWith(host);
    }

    @Override
    public String getStartUrl() {
        return String.format(url,i);
    }
}
