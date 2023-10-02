package com.sipc.hospitalalarmsystem.service;

import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.RealTimeAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmService {
    Boolean receiveAlarm(Integer cameraId,Integer caseType,String clipLink);

    List<SqlGetAlarmRes> queryAlarmList(Integer pageNum,Integer pageSize,Integer caseType, Integer status, Integer warningLevel, String time1,String time2);

    SqlGetAlarmRes getAlarm(Integer alarmId);

    Long getAlarmCnt(Integer caseType, String time1, String time2);

    Boolean updateAlarm(Integer alarmId, Boolean status, String processingContent);

    Boolean deleteAlarm(Integer alarmId);

    List<TimePeriod> getDayHistoryCnt(String date);

    List<TimePeriod> getThreeDaysHistoryCnt(String date);

    List<TimePeriod> getWeekHistoryCnt(String date);

    List<TimePeriod> getDayAreasHistoryCnt(String date);

    List<TimePeriod> getThreeDaysAreasHistoryCnt(String date);

    List<TimePeriod> getWeekAreasHistoryCnt(String date);

    List<TimePeriod> SqlGetCaseTypesDayHistoryCnt(String date);

    List<TimePeriod> SqlGetCaseTypesThreeDaysHistoryCnt( String date);

    List<TimePeriod> SqlGetCaseTypesWeekHistoryCnt( String date);


    RealTimeAlarmRes getRealTimeAlarmRes();


}
