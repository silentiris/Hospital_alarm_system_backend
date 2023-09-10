package com.sipc.hospitalalarmsystem.util;

public class TokenThreadLocalUtil {

    private ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    //new一个实例
    private static final TokenThreadLocalUtil instance = new TokenThreadLocalUtil();

    //私有化构造
    private TokenThreadLocalUtil() {
    }

    //获取单例
    public static TokenThreadLocalUtil getInstance() {
        return instance;
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
