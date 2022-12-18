package com.interceptly.api.util;

public abstract class RequestUtil {
    public static String clientOs(String userAgent){
        String clientOs = "";
        if (userAgent.toLowerCase().contains("windows"))
        {
            clientOs = "Windows";
        } else if(userAgent.toLowerCase().contains("mac"))
        {
            clientOs = "Mac";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            clientOs = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            clientOs = "Android";
        } else if(userAgent.toLowerCase().contains("iphone"))
        {
            clientOs = "IPhone";
        }else{
            clientOs = "UnKnown, More-Info: "+ userAgent;
        }
        return clientOs;
    }
    public static String browser(String userAgent){
        String browser = "";
        String user = userAgent.toLowerCase();
        if (user.contains("msie"))
        {
            String substring= userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else {
            String[] split = userAgent.substring(userAgent.indexOf("Version")).split(" ");
            if (user.contains("safari") && user.contains("version"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(split[0]).split("/")[1];
            } else if ( user.contains("opr") || user.contains("opera"))
            {
                if(user.contains("opera"))
                    browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(split[0]).split("/")[1];
                else if(user.contains("opr"))
                    browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            } else if (user.contains("chrome"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
            {
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (user.contains("firefox"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if(user.contains("rv"))
            {
                browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
            } else
            {
                browser = "UnKnown, More-Info: "+ userAgent;
            }
        }
        return browser;
    }
}
