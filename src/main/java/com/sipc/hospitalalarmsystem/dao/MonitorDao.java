package com.sipc.hospitalalarmsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.hospitalalarmsystem.model.po.Monitor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitorDao extends BaseMapper<Monitor> {

}
