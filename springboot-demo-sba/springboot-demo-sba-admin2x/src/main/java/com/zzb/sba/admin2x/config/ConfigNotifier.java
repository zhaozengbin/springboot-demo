package com.zzb.sba.admin2x.config;

import com.zzb.sba.admin2x.notifier.DingDingNotifier;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class ConfigNotifier {
    @Bean
    public DingDingNotifier customNotifier(InstanceRepository repository) {
        return new DingDingNotifier(repository);
    }
}
