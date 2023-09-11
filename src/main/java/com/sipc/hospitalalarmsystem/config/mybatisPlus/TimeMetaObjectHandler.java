package com.sipc.hospitalalarmsystem.config.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author CZCZCZ
 * &#064;date  2023-08-04 17:47
 */
@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(org.apache.ibatis.reflection.MetaObject metaObject) {
        this.setFieldValByName("createTime",new Timestamp(System.currentTimeMillis()), metaObject);
        this.setFieldValByName("updateTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }

    @Override
    public void updateFill(org.apache.ibatis.reflection.MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }

}
