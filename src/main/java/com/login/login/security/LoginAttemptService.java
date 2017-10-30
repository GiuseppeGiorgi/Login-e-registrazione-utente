package com.login.login.security;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by giuseppe on 22/10/17.
 */

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 10;
    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String key) throws Exception {
                return 0;
            }
        });
    }


    public void loginSuccessed(String key){
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key){
        int attempts = 0;

        try {
            attempts = attemptsCache.get(key);
        }
        catch (ExecutionException e){
            attempts = 0;
        }
        attempts ++;
        attemptsCache.put(key,attempts);
    }

    public boolean isBlocked(String key){
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        }
        catch (ExecutionException e){
            return false;
        }
    }
}
