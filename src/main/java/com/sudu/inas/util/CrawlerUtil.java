package com.sudu.inas.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrawlerUtil {

    public static final int MAXNUM = 2;

    public static final String BASE_A = "https://baike.baidu.com/item/";

    public static final String BASE_B = "https://baike.baidu.com/search/none?word=";


    public List<String> parseBaike(String url) throws IOException {
        String html = findHTML(url);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("body").select("div.body-wrapper").select("div.content-wrapper")
                .select("div").select("div.main-content").select("div.para");
        ArrayList<String> paraList = new ArrayList<>();
        for (Element e : elements) {
//            System.out.println(e.text());
            if (e.text().length()>0){
                paraList.add(e.text());}
        }
        return paraList;

    }

    public List<String> crawlerLink(String keyWord) throws IOException {
        String url = BASE_B + keyWord;
        String html = findHTML(url);
        Document doc = Jsoup.parse(html);
        ArrayList<String> list = new ArrayList<String>();
        List<String> urls = findBaikeLink(doc, list);
        return urls;

    }

    public static String findHTML(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String string = response.body().string();
        return string;
    }

    public static List<String> findBaikeLink(Document document, List<String> urls) throws IOException {
        int num = 0;
        String url = null;
        Elements links = document.select("[href*=/item]");
        for (Element link : links) {
            if(link.attr("href").contains("https")){
                url  = link.attr("href");
            }else{
                url  = "https://baike.baidu.com"+link.attr("href");
            }

            if (!urls.contains(url)) {
                urls.add(url);
                num++;
            }
            if (num > MAXNUM){
                break;
            }
        }
        return urls;
    }
}
