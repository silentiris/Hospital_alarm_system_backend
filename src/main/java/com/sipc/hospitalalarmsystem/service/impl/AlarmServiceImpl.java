package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.AlarmDao;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public GetAlarmRes getAlarm(Integer alarmId){
        return this.baseMapper.SqlGetAlarm(alarmId);
    }

    @Override
    public List<Alarm> queryAlarmList(Integer pageNum,Integer pageSize,Integer caseType, Integer status, Integer warningLevel, String processingContent, String time1, String time2) {
        QueryWrapper<Alarm> queryWrapper = new QueryWrapper<Alarm>();
        if (caseType != null)
            queryWrapper.eq("case_type", caseType);
        if (status != null)
            queryWrapper.eq("status", status);
        if (warningLevel != null)
            queryWrapper.eq("warning_level", warningLevel);
        if (processingContent != null)
            queryWrapper.like("processing_content", processingContent);

        if (time1 !=null && time2!=null)
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(time1);
                Date date2 = sdf.parse(time2);
                queryWrapper.between("time", date1, date2);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }

        if (time1!=null && time2==null)
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(time1);
                queryWrapper.ge("time", date1);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }

        if (time1==null && time2!=null)
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = sdf.parse(time2);
                queryWrapper.le("time", date2);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }

        Page<Alarm> page = new Page<>(pageNum, pageSize);
        IPage<Alarm> iPage = this.page(page, queryWrapper);
        return iPage.getRecords();
    }

    @Override
    public Long getAlarmCnt(Integer caseType, String time1,String time2){
        QueryWrapper<Alarm> queryWrapper = new QueryWrapper<Alarm>();

        if (caseType != null){
            queryWrapper.eq("case_type", caseType);
        }

        if (time1 !=null && time2!=null){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(time1);
                queryWrapper.ge("time", date1);
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }
        }

        if (time1==null && time2!=null){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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


}
