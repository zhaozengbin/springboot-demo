package com.zzb.core.utils;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

import java.util.Arrays;

public class DingdingUtils {
    private static final Log LOGGER = Log.get(DingdingUtils.class);

    public static void send(String token, String mdStr, String... phones) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=" + token);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown text = new OapiRobotSendRequest.Markdown();
        text.setText(mdStr);
        request.setMarkdown(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList(phones));
//        at.setIsAtAll(true);
        request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
            LOGGER.info("dingding 发送结果:" + JSON.toJSONString(response));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
