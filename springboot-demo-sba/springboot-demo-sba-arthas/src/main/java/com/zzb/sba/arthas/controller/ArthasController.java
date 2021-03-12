package com.zzb.sba.arthas.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.taobao.arthas.agent.attach.ArthasAgent;
import com.zzb.core.utils.EncryptionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/arthas")
public class ArthasController {
    private static final Log LOG = Log.get(ArthasController.class);

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public boolean start(@RequestBody String body) {
        String appId = null;
        String serverWsUrl = null;
        if (StrUtil.isNotEmpty(body)) {
            String bodyDecod = EncryptionUtils.SM4Decrypt(body);
            if (JSONUtil.isJson(bodyDecod)) {
                JSONObject bodyJSON = JSONUtil.parseObj(bodyDecod);
                try {
                    if (bodyJSON.containsKey("appId")) {
                        appId = bodyJSON.getStr("appId");
                    }
                    if (bodyJSON.containsKey("appId")) {
                        serverWsUrl = bodyJSON.getStr("serverWsUrl");
                    }
                    if (StrUtil.isEmpty(appId)) {
                        return false;
                    }
                    LOG.debug(String.format("准备启动arthas服务，服务器地址：[%s]，应用ID：[%s]",
                            serverWsUrl, appId));
                    arthasInit(appId, serverWsUrl);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    private void arthasInit(String appId, String serverWsUrl) throws Exception {
        HashMap<String, String> configMap = new HashMap<String, String>();
        configMap.put("arthas.agentId", appId);
        configMap.put("arthas.tunnelServer", serverWsUrl);
        configMap.put("arthas.telnetPort", "0");
        ArthasAgent.attach(configMap);
        LOG.info(String.format("启动arthas服务，服务器地址：[%s]，应用ID：[%s]",
                serverWsUrl, appId));

    }

}
