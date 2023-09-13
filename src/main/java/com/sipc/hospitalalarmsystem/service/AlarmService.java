package com.sipc.hospitalalarmsystem.service;

import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;

import java.util.Date;
import java.util.List;

public interface AlarmService {
    Boolean receiveAlarm(Integer cameraId,Integer caseType);

    List<Alarm> queryAlarmList(Integer pageNum,Integer pageSize,Integer caseType, Integer status, Integer warningLevel, String processingContent, String time1,String time2);

    GetAlarmRes getAlarm(Integer alarmId);

    Long getAlarmCnt(Integer caseType, String time1,String time2);

    Boolean updateAlarm(Integer alarmId, Boolean status, String processingContent);

    Boolean deleteAlarm(Integer alarmId);
}
