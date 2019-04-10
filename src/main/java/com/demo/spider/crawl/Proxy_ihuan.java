package com.demo.spider.crawl;

import com.demo.model.Proxy;
import com.demo.spider.IPageProcessor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;

@Component
public class Proxy_ihuan extends IPageProcessor {

    public String host = "https://ip.ihuan.me/";
    public String url = host;
    public int i = 0;

    private Map<Integer, String> mPages = new TreeMap<>();

    @Override
    public void process(Page page) {

        Selectable pages = page.getHtml().xpath("/html/body//div[2]/nav/ul/li");
        List<Selectable> nexts = pages.nodes();
        for (Selectable n : nexts) {
            String e = n.xpath("a/@href").toString().trim();
            if (!mPages.values().contains(e)) {
                mPages.put(mPages.size(), e);
            }
        }


        ArrayList<Proxy> proxys = null;
        Selectable tbody = page.getHtml().xpath("/html/body//div[2]/div[2]/table/tbody/tr");
        List<Selectable> tr = tbody.nodes();
        for (Selectable selectable : tr) {
            List<Selectable> nodes = selectable.xpath("td").nodes();
            if (isboolIp(nodes.get(0).xpath("td/a/text()").toString().trim())) {
                if (proxys == null) {
                    proxys = new ArrayList<>();
                }
                Proxy e = new Proxy();
                e.setIp(nodes.get(0).xpath("td/a/text()").toString().trim());
                e.setPort(Integer.parseInt(nodes.get(1).xpath("td/text()").toString().trim()));
                try {
                    e.setArea(nodes.get(2).xpath("td/a[1]/text()").toString().trim());
                } catch (Exception d) {
                }
                e.setCreatetime(new Date());
                e.setUpdatetime(new Date());
                proxys.add(e);

            }
        }
        if (proxys != null && proxys.size() > 0) {
            page.putField("proxys", proxys);
        } else {
            return;
        }

        if (i < mPages.size()) {
            i++;
            page.addTargetRequest(host + mPages.get(i));
        }

    }

    @Override
    public boolean isMatch(Page page) {
        return page.getRequest().getUrl().startsWith(host);
    }

    @Override
    public String getStartUrl() {
        return String.format(url, i);
    }
}
