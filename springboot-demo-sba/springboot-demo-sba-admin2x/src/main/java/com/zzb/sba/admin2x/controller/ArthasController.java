package com.zzb.sba.admin2x.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.zzb.core.utils.EncryptionUtils;
import com.zzb.core.utils.HttpUtils;
import com.zzb.sba.admin2x.config.ArthasProperties;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/arthas")
public class ArthasController {
    private static final Log LOG = Log.get(ArthasController.class);
    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private ArthasProperties arthasProperties;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("arthas/index");
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject start(String appId) {
        JSONObject result = new JSONObject();
        Mono<Instance> instanceMono = instanceRegistry.getInstance(InstanceId.of(appId));
        List<Instance> instanceFluxs = instanceMono.flux().collect(Collectors.toList()).blockOptional().get();
        if (CollUtil.isNotEmpty(instanceFluxs)) {
            Instance instanceFlux = instanceFluxs.get(0);
            String serviceUrl = instanceFlux.getRegistration().getServiceUrl();
            String serviceName = instanceFlux.getRegistration().getName();
            JSONObject body = new JSONObject();
            body.set("appId", appId);
            body.set("serverWsUrl", arthasProperties.getServerWsUrl());


            result.set("appId", appId);
            result.set("serviceName", serviceName);
            result.set("serverWsUrl", arthasProperties.getServerWsUrl());
            result.set("serviceUrl", serviceUrl);
            HttpResponse httpResponse = HttpUtils.post(serviceUrl + "/arthas/start", EncryptionUtils.SM4Encrypt(body.toString()));
            LOG.info(String.format("启动AppID：[%s]，url：[%s]的Arthas，启动反馈：[%s]", appId, serviceUrl, JSONUtil.parseObj(httpResponse)));
            if (httpResponse.getStatus() == 200) {
                String httpResult = httpResponse.body();
                if (StrUtil.isNotEmpty(httpResult) && Boolean.parseBoolean(httpResult)) {
                    Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
                    List<String> resultFindAll = ReUtil.getAllGroups(pattern, arthasProperties.getServerWsUrl());
                    String param = String.format("?ip=%s&port=%s&agentId=%s",
                            StrUtil.isEmpty(arthasProperties.getServerWsIp()) ? resultFindAll.get(1) : arthasProperties.getServerWsIp(),
                            StrUtil.isEmpty(arthasProperties.getServerWsPort()) ? resultFindAll.get(2) : arthasProperties.getServerWsPort(),
                            appId);
                    result.set("serverUrl", arthasProperties.getServerDomain() + param);
                } else {
                    result.set("msg", "启动arthas失败，输入appId有误或该应用没有继承arthas");
                }
            } else {
                result.set("msg", "启动arthas失败，输入appId有误或该应用没有继承arthas");
            }
        } else {
            result.set("msg", "输入appId有误或该应用没有继承springboot admin");
        }
        return result;
    }

}
