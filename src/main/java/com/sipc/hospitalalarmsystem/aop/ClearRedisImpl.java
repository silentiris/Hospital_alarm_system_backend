package com.sipc.hospitalalarmsystem.aop;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-08 22:31
 */
@Service
public class ClearRedisImpl
{
    @CacheEvict(value = "cache",allEntries = true)
    public void deleteCache(){
    }
}
