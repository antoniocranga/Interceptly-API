package com.interceptly.api.util;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RequestUtil {
    public static Map<String,Object> parseUserAgent(String userAgent) throws IOException, ParseException {
        final UserAgentParser parser = new UserAgentService().loadParser();
        final Capabilities capabilities = parser.parse(userAgent);
        final String browser = capabilities.getBrowser();
        final String browserType = capabilities.getBrowserType();
        final String browserMajorVersion = capabilities.getBrowserMajorVersion();
        final String deviceType = capabilities.getDeviceType();
        final String platform = capabilities.getPlatform();
        final String platformVersion = capabilities.getPlatformVersion();

        Map<String, Object> map = new HashMap<>();
        map.put("browser",browser);
        map.put("browserType",browserType);
        map.put("browserMajorVersion",browserMajorVersion);
        map.put("deviceType",deviceType);
        map.put("platform",platform);
        map.put("platformVersion",platformVersion);
        return map;
    }
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
    public static String browser(String userAgent) {
        String browser = "";
        return browser;
    }
}
