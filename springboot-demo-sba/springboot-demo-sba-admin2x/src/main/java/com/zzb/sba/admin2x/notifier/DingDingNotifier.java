package com.zzb.sba.admin2x.notifier;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.zzb.sba.admin2x.dingding.DingdingInfo;
import com.zzb.sba.admin2x.enums.EActiveProfile;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.Info;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 类名称：DingDingNotifier
 * 类描述：客户端服务状态监听类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/6 3:01 下午
 * 修改备注：TODO
 *
 */
public class DingDingNotifier extends AbstractStatusChangeNotifier {
    private static final Log LOG = Log.get(DingDingNotifier.class);
    private static EActiveProfile eActiveProfile = null;
    @Autowired
    private AbstractEnvironment environment;

    public DingDingNotifier(InstanceRepository repository) {
        super(repository);
    }

    private EActiveProfile getActiveProfile() {
        if (ObjectUtil.isEmpty(eActiveProfile)) {
            if (ArrayUtil.isNotEmpty(environment.getActiveProfiles())) {
                eActiveProfile = EActiveProfile.valueOfActiveProfile(environment.getActiveProfiles()[0]);
            } else {
                eActiveProfile = EActiveProfile.UNKNOWN;
            }
        }
        return eActiveProfile;
    }

    /**
     * 方法：doNotify
     * 描述：监听服务状态变化通知
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param event    :
     * @param instance :
     * @return : reactor.core.publisher.Mono<java.lang.Void>
     * @date: 2021年03月06日 3:01 下午
     */
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        InstanceId id = instance.getId();
        String serviceName = instance.getRegistration().getName();
        String serviceUrl = instance.getRegistration().getServiceUrl();
        String status = instance.getStatusInfo().getStatus();
        Map<String, Object> infoValues = null;
        if (ObjectUtil.isNotEmpty(instance.getInfo())) {
            Info info = instance.getInfo();
            if (ObjectUtil.isNotEmpty(info.getValues())) {
                infoValues = info.getValues();
            }
        }
        DingdingInfo dingdingInfo = DingdingInfo.getInstance(infoValues);
        Map<String, Object> details = instance.getStatusInfo().getDetails();
        return Mono.fromRunnable(() -> {
            if (getActiveProfile().isSend()) {
                dingdingInfo.send(serviceName, serviceUrl, id.getValue(), status, details);
            } else {
                String msg = "服务名称：[%s],服务地址：[%s],服务ID：[%s],服务环境：[%s],服务状态：[%s],服务详细信息：[%s]";
                LOG.info(String.format(msg, serviceName, serviceUrl, id, getActiveProfile().name(), status, details));
            }
        });

    }
}