package com.zzb.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class DingdingUtils {
    private static final Log LOG = Log.get(DingdingUtils.class);

    private static final String DINGDING_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s";
    private static final String DINGDING_URL_SIGN = "&timestamp=%d&sign=%s";

    public static void send(String token, String secret, List<String> phones, boolean atAll, String mdStr) {
        String url = String.format(DINGDING_URL, token) + signUrl(secret);
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown text = new OapiRobotSendRequest.Markdown();
        text.setTitle("异常提醒");
        text.setText(mdStr);
        request.setMarkdown(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();

        at.setIsAtAll(atAll);
        at.setAtMobiles(phones);
        request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
            LOG.info("dingding 发送结果:" + JSON.toJSONString(response));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private static String signUrl(String secret) {
        if (StrUtil.isNotEmpty(secret)) {
            long timestamp = System.currentTimeMillis();
            String sign = generateSign(timestamp, secret);
            if (StrUtil.isNotEmpty(sign)) {
                return String.format(DINGDING_URL_SIGN, timestamp, sign);
            }
        }
        return "";
    }

    private static String generateSign(long timestamp, String secret) {
        try {
            String combine = String.format("%d\n%s", timestamp, secret);
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
                byte[] signData = mac.doFinal(combine.getBytes("UTF-8"));
                return Base64.encodeBase64String(signData);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
