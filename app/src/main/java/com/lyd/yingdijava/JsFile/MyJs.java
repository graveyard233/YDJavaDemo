package com.lyd.yingdijava.JsFile;

public class MyJs {
    public static final String getHtml = "javascript:function getHtmlCode(){" +
            "var content = document.getElementsByTagName('html')[0].innerHTML;" +
            "return '<html>' + content + '</html>'" +
            "}";

    public static final String removeFloatBar = "javascript: document.getElementsByClassName('down-load-app-container-post')[0].style.display = 'none';";
//            "javascript: document.getElementsByClassName('down-load-app-container-post')[0].innerHTML = " +
//            "'" +
////            "<font style=\"float:left;margin-left:5px;font-size:.42rem;margin-top:3px;\">旅法师营地</font>" +
//            "<p>旅法师营地</p>" +
//            "';";

    public static final String removeOpenApp = "javascript: document.getElementsByClassName('open-app-box')[0].style.display = 'none';";

}
