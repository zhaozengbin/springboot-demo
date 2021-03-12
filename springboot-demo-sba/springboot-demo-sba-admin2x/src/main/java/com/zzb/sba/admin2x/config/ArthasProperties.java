package com.zzb.sba.admin2x.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component // 配置 Bean 被 Spring 容器管理
@ConfigurationProperties(prefix = "arthas") // 配置文件和实体进行映射，配置前缀，这里对应 yml 文件中的对象名
@Data
public class ArthasProperties {
    private String serverDomain;
    private String serverWsUrl;
    private String serverWsIp;
    private String serverWsPort;
}
