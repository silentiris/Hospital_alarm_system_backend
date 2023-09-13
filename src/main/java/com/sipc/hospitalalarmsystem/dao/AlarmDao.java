package com.sipc.hospitalalarmsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-11 19:29
 */
@Mapper
public interface AlarmDao extends BaseMapper<Alarm>{

    @Select("SELECT monitor.area, alarm_info.* FROM monitor INNER JOIN alarm_info ON monitor.id = alarm_info.monitor_id WHERE alarm_info.id = #{alarmId}")
    GetAlarmRes SqlGetAlarm(@Param("alarmId") Integer alarmId);

}
