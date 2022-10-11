package com.lyd.yingdijava.Info;

import java.util.HashMap;
import java.util.Map;

public class UrlInfo {
    private static UrlInfo instances;

    private HashMap<String,String> map = new HashMap<>();

    private UrlInfo(){
        if (map.isEmpty()){
            map.put("炉石","https://iyingdi.com/tz/tag/17");
            map.put("玩家杂谈","https://iyingdi.com/tz/tag/23");
        }
    }

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
