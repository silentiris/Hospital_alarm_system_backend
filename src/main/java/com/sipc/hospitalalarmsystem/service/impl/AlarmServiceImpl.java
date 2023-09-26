package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.AlarmDao;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.RealTimeAlarmRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.SqlGetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AlarmServiceImpl extends ServiceImpl<AlarmDao, Alarm> implements AlarmService {



    @Override
    public Boolean receiveAlarm(Integer cameraID, Integer caseType) {
        log.info("receive alarm from camera: "+cameraID+" caseType: "+caseType);
        log.info("Start recording....");
        new Thread(()->{
//            record(id)
            Alarm alarm = new Alarm();
            alarm.setClipLink("");//后面放文件名
            alarm.setCaseType(caseType);
            alarm.setMonitorId(cameraID);
            alarm.setWarningLevel(1);
            alarm.setStatus(false);
            this.save(alarm);
        }).start();
        return true;
    }

    @Override
    public SqlGetAlarmRes getAlarm(Integer alarmId){

        return this.baseMapper.SqlGetAlarm(alarmId);
    }


    public  List<SqlGetAlarmRes> queryAlarmList(Integer pageNum,Integer pageSize,Integer caseType, Integer status, Integer warningLevel, String time1,String time2) {

//        if (caseType != null)
//            queryWrapper.eq("caseType", caseType);
//        if (status != null)
//            queryWrapper.eq(, status);
//        if (warningLevel != null)
//            queryWrapper.eq(, warningLevel);
//        if (processingContent != null)
//            queryWrapper.like(Alarm::getProcessingContent, processingContent);
//
//        if (time1 !=null && time2!=null)
//            try{
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date1 = sdf.parse(time1);
//                Date date2 = sdf.parse(time2);
//                queryWrapper.between(Alarm::getCreateTime, date1, date2);
//            } catch (ParseException e) {
//                log.error(e.getMessage());
//                return null;
//            }
//
//        if (time1!=null && time2==null)
//            try{
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date1 = sdf.parse(time1);
//                queryWrapper.ge(Alarm::getCreateTime, date1);
//            } catch (ParseException e) {
//                log.error(e.getMessage());
//                return null;
//            }
//
//        if (time1==null && time2!=null)
//            try{
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date2 = sdf.parse(time2);
//                queryWrapper.le(Alarm::getCreateTime, date2);
//            } catch (ParseException e) {
//                log.error(e.getMessage());
//                return null;
//            }

//        Page<Alarm> page = new Page<>(pageNum, pageSize);
//        IPage<Alarm> iPage = this.page(page, queryWrapper);

        List<SqlGetAlarmRes> alarms = this.baseMapper.selectByCondition(pageNum-1,pageSize,caseType != null?caseType.toString():null, status != null?status.toString():null, warningLevel!=null?warningLevel.toString():null, time1, time2);

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
    public RealTimeAlarmRes getRealTimeAlarmRes(){
        RealTimeAlarmRes realTimeAlarmRes = new RealTimeAlarmRes();
        realTimeAlarmRes.setAlarmTotal(this.baseMapper.SqlGetAlarmTotal());
        realTimeAlarmRes.setAlarmCaseTypeTotalList(this.baseMapper.SqlGetAlarmCaseTypeTotal());

        return realTimeAlarmRes;
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
