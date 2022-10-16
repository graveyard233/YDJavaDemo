package com.lyd.yingdijava.Utils;

public class TextUtils {
    public static String getImageUrlFromStyle(String style){
        StringBuffer sb = new StringBuffer(style);
        sb.delete(0,sb.indexOf("http"));
        sb.delete(sb.length() - 3,sb.length());
        return sb.toString();
    }

    public static int getCountFromString(String str,String key){
        int count = 0;
        int index = 0;
        if (key == null || key.equals("") )
            return 0;
        while ((index = str.indexOf(key,index)) != -1){
            index += key.length();
            count++;
        }
        return count;
    }
}
