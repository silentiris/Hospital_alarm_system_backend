package com.sipc.hospitalalarmsystem.util;

import lombok.Getter;

public class TokenThreadLocalUtil {

    private ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    //获取单例
    //new一个实例
    @Getter
    private static final TokenThreadLocalUtil instance = new TokenThreadLocalUtil();

    //私有化构造
    private TokenThreadLocalUtil() {
    }

    public void bind(String token) {
        tokenThreadLocal.set(token);
    }

    public String getToken() {
        String token = tokenThreadLocal.get();
        remove();
        return token;
    }

    public void remove() {
        tokenThreadLocal.remove();
    }
}
