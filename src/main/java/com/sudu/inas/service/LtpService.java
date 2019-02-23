package com.sudu.inas.service;

import com.sudu.inas.beans.Action;
import com.sudu.inas.beans.Arg;
import com.sudu.inas.beans.SyntaxResult;
import com.sudu.inas.beans.Word;



import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
public class LtpService {


    private final String basicUrl = "http://192.168.11.210:8020/ltp";

    private final String defaultParam = "t=all&x=n";

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                System.out.println();
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    public SyntaxResult getLtpResult(String text) {
        String para = "s=" + text + "&" + defaultParam;
        String result = sendPost(basicUrl, para);

        HashMap<Integer, Word> words = new HashMap<>();
        ArrayList<Word> verbs = new ArrayList<>();
        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<Integer> nh = new ArrayList<>();
        String replace = result.replace(" ", "");
        System.out.println(replace);
        String s = replace.substring(2, replace.length() - 2);
        System.out.println(s);
//        String s = result.substring(5, result.length() - 2);
        JSONArray array = JSONArray.fromObject(s);
        for (int j= 0;j<array.size();j++){
            JSONObject object = array.getJSONObject(j);
            Word word = new Word();
            word.setCont(object.getString("cont"));
            word.setId(object.getInt("id"));
            word.setRelate(object.getString("relate"));
            word.setPos(object.getString("pos"));
            word.setParent(object.getInt("parent"));
            if ("ns".equals(word.getPos())){
                position.add(word.getId());
            }
            if ("nh".equals(word.getPos())){
                nh.add(word.getId());
            }
            word.setNe(object.getString("ne"));
            if ("HED".equals(word.getRelate())){
                Word verb = parseVerb(word, object);
                words.put(verb.getId(),verb);
                verbs.add(verb);
            }else if ("COO".equals(word.getRelate())){
                Word verb = parseVerb(word, object);
                words.put(verb.getId(),verb);
                verbs.add(verb);
            }else {
                words.put(word.getId(),word);
            }
        }


        SyntaxResult syntaxResult = new SyntaxResult();
        syntaxResult.setNh(nh);
        syntaxResult.setPos(position);
        syntaxResult.setVerbs(verbs);
        syntaxResult.setWords(words);

        return syntaxResult;
    }

    public Word parseVerb(Word word, JSONObject object){
        JSONArray args = object.getJSONArray("arg");
        ArrayList<Arg> argsList = new ArrayList<>();
        for (int i = 0;i<args.size();i++){
            JSONObject o = args.getJSONObject(i);
            Arg arg = new Arg();
            arg.setId(o.getInt("id"));
            arg.setBegin(o.getInt("beg"));
            arg.setEnd(o.getInt("end"));
            arg.setType(o.getString("type"));
            argsList.add(arg);
        }
        word.setArgs(argsList);
        return word;
    }

    public static void main(String[] args) {
        LtpService ltpService = new LtpService();
//        ltpService.getLtpResult("1991年，奥巴马以优等生荣誉从哈佛法学院毕业。");
        SyntaxResult ltpResult = ltpService.getLtpResult("2008年11月4日，奥巴马正式当选美国总统。");
        try {
            List<Action> dotrans = ltpResult.dotrans();
            for (Action a:dotrans){
                System.out.println(a);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
