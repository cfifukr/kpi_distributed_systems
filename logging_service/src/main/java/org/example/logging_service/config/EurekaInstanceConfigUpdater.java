package org.example.logging_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaInstanceConfigUpdater implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private EurekaInstanceConfigBean eurekaInstanceConfig;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int actualPort = event.getWebServer().getPort();
        System.out.println("EurekaInstanceConfigUpdater port: " + actualPort);
        eurekaInstanceConfig.setInstanceId(appName + ":" + actualPort);
        eurekaInstanceConfig.setNonSecurePort(actualPort);
    }
}