package com.sipc.hospitalalarmsystem.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings("all")
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入String缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设置缓存过期时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     * @param timeUnit   timeUnit
     * @return boolean
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 从左边向一个列表push一个元素
     *
     * @param listName
     * @param value
     * @return boolean
     */
    public boolean listLeftPush(String listName, Object value) {
        boolean result = false;
        try {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            operations.leftPush(listName, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 从右边从一个列表pop一个元素
     *
     * @param listName
     * @return boolean
     */
    public boolean listRightPop(String listName) {
        boolean result = false;
        try {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            operations.rightPop(listName);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 找到了返回index，找不到返回-1
     * @param id
     * @return {@code List<PrivateChatLog>}
     */
//    public long getIndexOfLogId(String key,long  id){
//        ListOperations<String, Object> operations = redisTemplate.opsForList();
//        for(long i = 0;i< operations.size(key);i++){
//            if(((PrivateChatLog)operations.index(key,i)).getId()==id){
//                return i;
//            }
//        }
//        return -1;
//    }

    /**
     * 获得list长度
     *
     * @param listName
     * @return long
     */
    public long listLen(String listName) {
        try {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            return operations.size(listName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List listRange(String listName, long start, long end) {
        try {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            return operations.range(listName, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 键
     * @return boolean
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 取出缓存
     *
     * @param key 键
     * @return 缓存
     */
    public Object get(final String key) {
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 向zet中添加元素
     * @param key
     * @param value
     * @param score
     * @return boolean
     */
    public boolean addZsetValue(final String key,final Object value,final double score){
        ZSetOperations<String,Object> operations = redisTemplate.opsForZSet();
        try {
            operations.add(key,value,score);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteZsetValue(final String key,final Object... values){
        ZSetOperations<String,Object> operations = redisTemplate.opsForZSet();
        try {
            operations.remove(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return {@code Set<Object>}
     */
    public Set<Object> reRangeByScore(String key, double min, double max){
        ZSetOperations<String,Object> operations = redisTemplate.opsForZSet();
        try {
            Set<Object> objectSet = operations.reverseRangeByScore(key, min, max);
            return objectSet;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}