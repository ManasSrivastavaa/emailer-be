package com.manas.emailer.util;


import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class TokenManager {

    private static final Cache<String, String> tokenCache = Caffeine.newBuilder()
            .expireAfterWrite(55, TimeUnit.MINUTES) 
            .build();

    private TokenManager() {}

    public static void saveToken(String key, String value) {
        tokenCache.put(key, value);
    }

    public static String getToken(String key) {
        return tokenCache.getIfPresent(key);
    }

    public static void removeToken(String key) {
        tokenCache.invalidate(key);
    }

    public static boolean containsToken(String key) {
        return tokenCache.getIfPresent(key) != null;
    }

    public static void clearAll() {
        tokenCache.invalidateAll();
    }
}
