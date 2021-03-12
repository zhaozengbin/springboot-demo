package com.zzb.core.utils;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
    /**
     * 获取当前访问URL （含协议、域名、端口号[忽略80端口]、项目名）
     *
     * @param request
     * @return: String
     */
    public static String getServerUrl(HttpServletRequest request) {
        // 访问协议
        String agreement = request.getScheme();
        // 访问域名
        String serverName = request.getServerName();
        // 访问端口号
        int port = request.getServerPort();
        // 访问项目名
        String contextPath = request.getContextPath();
        String url = "%s://%s%s%s";
        String portStr = "";
        if (port != 80) {
            portStr += ":" + port;
        }
        return String.format(url, agreement, serverName, portStr, contextPath);

    }

    public static String get(String serverUrl) {
        return HttpUtil.get(serverUrl);
    }

    public static HttpResponse post(String serverUrl, JSONObject body) {
        return post(serverUrl, body.toString());
    }

    public static HttpResponse post(String serverUrl, String body) {
        HttpRequest httpRequest = HttpUtil.createPost(serverUrl);
        httpRequest.body(body, ContentType.JSON.getValue());
        return httpRequest.execute();
    }
}
