package com.zzb.sba.admin2x.dingding;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.zzb.core.utils.DingdingUtils;
import com.zzb.core.utils.markdown.SimpleMarkdownBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类名称：DingdingInfo
 * 类描述：钉钉发送信息类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/6 3:00 下午
 * 修改备注：TODO
 *
 */
@Data
@AllArgsConstructor
public class DingdingInfo {


    private final static String DEFAULT_AT_NAME = "at";
    private final static String DEFAULT_TOKEN_NAME = "token";
    private final static String DEFAULT_SECRET_NAME = "secret";

    private final static String DEFAULT_TOKEN = "a456b3ae219e760b021509bbdec37ed62c56d5d33ff6345c71d1fc26f0ec3f29";
    private final static String DEFAULT_SECRET = "SECa26d9f5128da05852c4ba432ecf4e0d2dbdbc5ff5037ac88b2f042889cf2c04f";

    private List<String> at;
    private boolean atAll;
    private String token;
    private String secret;

    /**
     * 方法：getInstance
     * 描述：通过客户端组装钉钉信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param infoValues :
     * @return : com.zzb.sba.admin2x.dingding.DingdingInfo
     * @date: 2021年03月06日 3:00 下午
     */
    public static DingdingInfo getInstance(Map<String, Object> infoValues) {
        DingdingInfo dingdingInfo = new DingdingInfo(null, true, DEFAULT_TOKEN, DEFAULT_SECRET);
        if (MapUtil.isNotEmpty(infoValues) && infoValues.containsKey("dingding")) {
            Map<String, Object> dingding = null;
            Object dingdingObj = infoValues.get("dingding");
            if (dingdingObj instanceof Map) {
                dingding = (Map<String, Object>) infoValues.get("dingding");
            }
            if (MapUtil.isNotEmpty(dingding)) {
                if (dingding.containsKey(DEFAULT_AT_NAME)) {
                    dingdingInfo.setAt(CollUtil.newArrayList(dingding.get(DEFAULT_AT_NAME).toString().split(",")));
                    dingdingInfo.setAtAll(false);
                }
                if (dingding.containsKey(DEFAULT_TOKEN_NAME)) {
                    dingdingInfo.setToken(dingding.get(DEFAULT_TOKEN_NAME).toString());
                }
                if (dingding.containsKey(DEFAULT_SECRET_NAME)) {
                    dingdingInfo.setSecret(dingding.get(DEFAULT_SECRET_NAME).toString());
                }
            }
        }
        return dingdingInfo;
    }

    /**
     * 方法：send
     * 描述：组装钉钉题型内容 并发送
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param serviceName :
     * @param serviceUrl  :
     * @param serviceId   :
     * @param status      :
     * @param details     :
     * @date: 2021年03月06日 3:00 下午
     */
    public void send(String serviceName, String serviceUrl, String serviceId, String status, Map<String, Object> details) {
        SimpleMarkdownBuilder simpleMarkdownBuilder = SimpleMarkdownBuilder.create().title("系统监控", 1);
        simpleMarkdownBuilder.title("服务名称：" + serviceName, 2);
        simpleMarkdownBuilder.title("报告时间：" + DateUtil.now(), 2);
        simpleMarkdownBuilder.title("服务ID：" + serviceId, 2);
        simpleMarkdownBuilder.title("服务地址：" + serviceUrl, 2);

        if (CollUtil.isNotEmpty(this.getAt())) {
            simpleMarkdownBuilder.title("负责人：", 2).at(this.getAt());
        }

        simpleMarkdownBuilder.title("服务状态：" + status, 2);
        simpleMarkdownBuilder.title("服务详情：", 2);
        details.entrySet().stream().forEach(entity -> {
            String key = entity.getKey();
            Object value = entity.getValue();
            Map<String, Object> mapValue = null;
            if (value instanceof Map) {
                mapValue = (Map<String, Object>) value;
                simpleMarkdownBuilder.title(key + "：" + mapValue.get("status"), 3);
                simpleMarkdownBuilder.text("detail：" + mapValue.get("details"), true);
            } else {
                simpleMarkdownBuilder.text(key + "：" + value, true);
            }
        });
        DingdingUtils.send(this.getToken(), this.getSecret(), this.getAt(), true, simpleMarkdownBuilder.build());
    }
}
