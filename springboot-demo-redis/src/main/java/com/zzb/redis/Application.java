package com.zzb.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 类名称：EsDemoApplication
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:37 上午
 * 修改备注：TODO
 */
@SpringBootApplication(scanBasePackages = "com.zzb")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
