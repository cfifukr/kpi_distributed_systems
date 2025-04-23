package org.example.logging_service.config;

import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HazelcastEurekaLifecycleListener implements LifecycleListener {

    private final EurekaHazelcastRegistrar registrar;

    @Autowired
    public HazelcastEurekaLifecycleListener(EurekaHazelcastRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
        if (event.getState() == LifecycleEvent.LifecycleState.STARTED) {
            registrar.register();
        } else if (event.getState() == LifecycleEvent.LifecycleState.SHUTTING_DOWN) {
            registrar.deregister();
        }
    }
}
