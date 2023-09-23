package com.sipc.hospitalalarmsystem.dao;

import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.SqlGetAlarmRes;
import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-21 14:19
 */
@Mapper
public interface QueryAlarmListDao extends JoinBaseMapper<SqlGetAlarmRes> {
}
