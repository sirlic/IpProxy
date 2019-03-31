package com.demo.spider.crawl;

import com.demo.model.Proxy;
import com.demo.spider.IPageProcessor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Proxy_66ip extends IPageProcessor {

    public String host = "http://www.66ip.cn";
    public  String url = host + "/%d.html";
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
    public boolean isMatch(Page page) {
        return page.getRequest().getUrl().startsWith(host);
    }

    @Override
    public String getStartUrl() {
        return String.format(url,i);
    }
}
