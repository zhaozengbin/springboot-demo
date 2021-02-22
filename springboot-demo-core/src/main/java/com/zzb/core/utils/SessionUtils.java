package com.zzb.core.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static HttpSession getSession() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            return session;
        } catch (Exception e) {

        }
        return null;
    }

    public static String getSessionId() {
        HttpSession session = getSession();
        if (ObjectUtil.isNotEmpty(getSession())) {
            return session.getId();
        }
        return null;
    }
}
