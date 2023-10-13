package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.AlarmDao;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetHistoryCntRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.RealTimeAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import com.sipc.hospitalalarmsystem.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class AlarmServiceImpl extends ServiceImpl<AlarmDao, Alarm> implements AlarmService {

    @Autowired
    MonitorServiceImpl monitorServiceImpl;

    @Autowired
    OssUtil ossUtil;

    @Override
    public Boolean receiveAlarm(Integer cameraID, Integer caseType,String clipLink){
        log.info("receive alarm from camera: "+cameraID+" caseType: "+caseType);
        monitorServiceImpl.getBaseMapper().MonitorAlarmCntPlusOne(cameraID);
        Alarm alarm = new Alarm();
        alarm.setClipLink(clipLink);
        alarm.setCaseType(caseType);
        alarm.setMonitorId(cameraID);
        alarm.setWarningLevel(1);
        alarm.setStatus(false);
        this.save(alarm);
        return true;
    }

    @Override
    public SqlGetAlarm getAlarm(Integer alarmId){
        SqlGetAlarm sqlGetAlarm = this.baseMapper.SqlGetAlarm(alarmId);
        sqlGetAlarm.setClipLink(ossUtil.getClipLinkByUuid(sqlGetAlarm.getClipLink()));
        return sqlGetAlarm;
    }


    public  List<SqlGetAlarm> queryAlarmList(Integer pageNum, Integer pageSize, Integer caseType, Integer status, Integer warningLevel, String time1, String time2) {
        List<SqlGetAlarm> alarms = this.baseMapper.selectByCondition((pageNum - 1) * pageSize,pageSize,caseType != null?caseType.toString():null, status != null?status.toString():null, warningLevel!=null?warningLevel.toString():null, time1, time2);
        for (SqlGetAlarm alarm : alarms) {
            alarm.setClipLink(ossUtil.getClipLinkByUuid(alarm.getClipLink()));
        }
        //根据warningLevel和时间进行降序排序
        if (alarms.isEmpty())
            return alarms;

        return alarms;
    }

    @Override
    public Long getAlarmCnt(Integer caseType, String time1,String time2){
        QueryWrapper<Alarm> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (caseType != null){
            queryWrapper.eq("case_type", caseType);
        }

        if (time1 !=null && time2!=null){
            try{
                Date date1 = sdf.parse(time1);
                Date date2 = sdf.parse(time2);
                queryWrapper.between("time", date1, date2);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }
        }

        if(time1!=null && time2==null){
            try{
                Date date1 = sdf.parse(time1);
                queryWrapper.ge("time", date1);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }
        }

        if (time1==null && time2!=null){
            try{
                Date date2 = sdf.parse(time2);
                queryWrapper.le("time", date2);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }
        }


        return this.count(queryWrapper);

    }

    @Override
    public Boolean updateAlarm(Integer alarmId, Boolean status, String processingContent){
        Alarm alarm = this.getById(alarmId);
        if (alarm == null)
            return false;
        alarm.setStatus(status);
        alarm.setProcessingContent(processingContent);
        return this.updateById(alarm);
    }

    @Override
    public Boolean deleteAlarm(Integer alarmId){
        return this.removeById(alarmId);
    }

    @Override
    public List<TimePeriod> getDayHistoryCnt(String date){
        List<TimePeriod> timePeriods = this.baseMapper.SqlGetDayHistoryCnt(date);

        // 生成一个包含所有时间段的完整列表
        List<String> allPeriods = Arrays.asList("03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "24:00");

        //对齐补齐时间
        Alignment(allPeriods, timePeriods);

        return timePeriods;

    }

    @Override
    public List<TimePeriod> getDayAreasHistoryCnt(String date){
        return this.baseMapper.SqlGetAreasDayHistoryCnt(date);
    }

    @Override
    public List<TimePeriod> getThreeDaysHistoryCnt(String date){
        List<String> allPeriods = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 3; i++) {
            allPeriods.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        List<TimePeriod> timePeriods = this.baseMapper.SqlGetThreeDaysHistoryCnt(date);
        Alignment(allPeriods, timePeriods);
        //只保留月份和日期
        for (TimePeriod tp : timePeriods) {
            tp.setPeriod(tp.getPeriod().substring(5));
        }
        return timePeriods;
    }

    @Override
    public List<TimePeriod> getWeekHistoryCnt(String date){
        List<String> allPeriods = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            allPeriods.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        List<TimePeriod> timePeriods = this.baseMapper.SqlGetWeekHistoryCnt(date);
        Alignment(allPeriods, timePeriods);
        //只保留月份和日期
        for (TimePeriod tp : timePeriods) {
            tp.setPeriod(tp.getPeriod().substring(5));
        }
        return timePeriods;
    }

    @Override
    public List<TimePeriod> getThreeDaysAreasHistoryCnt(String date){
        return this.baseMapper.SqlGetAreasThreeDaysHistoryCnt(date);
    }

    @Override
    public List<TimePeriod> getWeekAreasHistoryCnt(String date){
        return this.baseMapper.SqlGetAreasWeekHistoryCnt(date);
    }


    @Override
    @Cacheable(value = "cache",key = "'getRealTimeAlarmRes'",unless = "#result==null")
    public RealTimeAlarmRes getRealTimeAlarmRes(){
        RealTimeAlarmRes realTimeAlarmRes = new RealTimeAlarmRes();
        try{
            realTimeAlarmRes.setAlarmTotal(this.baseMapper.SqlGetAlarmTotal());
            realTimeAlarmRes.setAlarmCaseTypeTotalList(this.baseMapper.SqlGetAlarmCaseTypeTotal());

            return realTimeAlarmRes;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }

    }

    @Override
    public List<TimePeriod> SqlGetCaseTypesDayHistoryCnt(String date){
        return this.baseMapper.SqlGetCaseTypesDayHistoryCnt(date);
    }

    @Override
    public List<TimePeriod> SqlGetCaseTypesThreeDaysHistoryCnt( String date){
        return this.baseMapper.SqlGetCaseTypesThreeDaysHistoryCnt(date);
    }

    @Override
    public List<TimePeriod> SqlGetCaseTypesWeekHistoryCnt( String date){
        return this.baseMapper.SqlGetCaseTypesWeekHistoryCnt(date);
    }

    @Override
    @Cacheable(value = "cache",key = "'ServiceGetHistoryCntRes'+#defer",unless = "#result==null")
    public GetHistoryCntRes ServiceGetHistoryCntRes(Integer defer){
        GetHistoryCntRes getHistoryCntRes = new GetHistoryCntRes();
        List<TimePeriod> g1;
        List<TimePeriod> g2;
        List<TimePeriod> g3;
        LocalDateTime currentDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).minusDays(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = currentDate.format(formatter);
        if (defer == 1){
            g1 = this.getDayHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = this.getDayAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = this.SqlGetCaseTypesDayHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
            return getHistoryCntRes;
        }
        else if (defer == 3){
            g1 = this.getThreeDaysHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = this.getThreeDaysAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = this.SqlGetCaseTypesThreeDaysHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
            return getHistoryCntRes;
        }
        else if (defer == 7){
            g1 = this.getWeekHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = this.getWeekAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = this.SqlGetCaseTypesWeekHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
            return getHistoryCntRes;
        }
        else{
            return null;
        }
    }

    private List<TimePeriod> Alignment(List<String> allPeriods,List<TimePeriod> timePeriods){
        for (String period : allPeriods) {
            boolean exists = false;
            for (TimePeriod tp : timePeriods) {
                if (tp.getPeriod().equals(period)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                TimePeriod newTp = new TimePeriod();
                newTp.setPeriod(period);
                newTp.setCnt(0L);
                timePeriods.add(newTp);
            }
        }
        return timePeriods;
    }

}
