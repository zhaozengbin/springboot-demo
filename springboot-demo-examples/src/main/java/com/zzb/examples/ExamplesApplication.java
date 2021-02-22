package com.zzb.examples;

import com.hankcs.hanlp.HanLP;
import com.zzb.monitor.chain.aop.ann.ChainAnn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 类名称：EsDemoApplication
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:37 上午
 * 修改备注：TODO
 */
@SpringBootApplication(scanBasePackages = "com.zzb")
@EnableElasticsearchRepositories(basePackages = "com.zzb.es.repository")
@ChainAnn
public class ExamplesApplication {
    static {
        HanLP.Config.enableDebug(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExamplesApplication.class, args);
    }
}
