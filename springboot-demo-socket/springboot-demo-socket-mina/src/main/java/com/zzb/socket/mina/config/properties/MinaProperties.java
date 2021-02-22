package com.zzb.socket.mina.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 类名称：MinaProperties
 * 类描述：MINA配置相关信息
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/19 6:03 下午
 * 修改备注：TODO
 */
@Data
@Component
public class MinaProperties {

    @Value("${socket.mina.port}")
    private int port;
}

