package com.lyd.yingdijava.Info;

import java.util.HashMap;
import java.util.Map;

public class UrlInfo {
    private static UrlInfo instances;

    private HashMap<String,String> map = new HashMap<>();

    private UrlInfo(){
        if (map.isEmpty()){
            map.put("webView","https://mob.iyingdi.com");
            map.put("炉石","https://iyingdi.com/tz/tag/17");
            map.put("炉石mob","https://mob.iyingdi.com/fine/17");
            map.put("玩家杂谈","https://iyingdi.com/tz/tag/23");
            map.put("玩家杂谈mob","https://mob.iyingdi.com/fine/23");
            map.put("万智牌","https://iyingdi.com/tz/tag/18");
            map.put("万智牌mob","https://mob.iyingdi.com/fine/18");
        }
    }

    //单例
    public static UrlInfo getInstance(){
        if (instances == null){
            synchronized (UrlInfo.class){
                if (instances == null){
                    instances = new UrlInfo();
                }
            }
        }
        return instances;
    }

    public String getUrlByKey(String key){
        return map.get(key);
    }

}
