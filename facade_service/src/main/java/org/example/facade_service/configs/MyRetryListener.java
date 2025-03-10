package org.example.facade_service.configs;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
public class MyRetryListener implements RetryListener {
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        int attempt = context.getRetryCount();
        System.out.println("Retry Attempt: " + attempt + " | Exception: " + throwable.getMessage());    }
}


