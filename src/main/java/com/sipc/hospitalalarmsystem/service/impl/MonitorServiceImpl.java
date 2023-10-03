package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.MonitorDao;
import com.sipc.hospitalalarmsystem.model.dto.param.Monitor.CreateMonitorParam;
import com.sipc.hospitalalarmsystem.model.dto.param.Monitor.UpdateMonitorParam;
import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import com.sipc.hospitalalarmsystem.service.RequestFlaskService;
import com.sipc.hospitalalarmsystem.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorDao,Monitor> implements MonitorService {

    @Autowired
    public RequestFlaskService requestFlaskService;

    @Override
    public List<Monitor> getMonitorList() {
        return this.list();
    }

    @Override
    public Monitor getMonitorById(Integer id) {
        return this.getById(id);
    }

    @Override
    public Integer createMonitor(CreateMonitorParam createMonitorParam) {
        Monitor monitor = new Monitor();
        monitor.setName(createMonitorParam.getName());
        monitor.setArea(createMonitorParam.getArea());
        monitor.setLeader(createMonitorParam.getLeader());
        monitor.setRunning(true);
        monitor.setAlarmCnt(0);
        monitor.setLatitude(createMonitorParam.getLatitude());
        monitor.setLongitude(createMonitorParam.getLongitude());
        monitor.setFall(false);
        monitor.setFlame(false);
        monitor.setSmoke(false);
        monitor.setSmoking(false);
        monitor.setDangerArea(false);
        monitor.setStreamLink(createMonitorParam.getIp());
        monitor.setLeftX(createMonitorParam.getLeftX());
        monitor.setLeftY(createMonitorParam.getLeftY());
        monitor.setRightX(createMonitorParam.getRightX());
        monitor.setRightY(createMonitorParam.getRightY());
        try{
            save(monitor);
            return monitor.getId();

        }catch (Exception e){
            log.error("创建监控失败");
            return -1;
        }
    }

    @Override
    public String getMonitorIPById(Integer id){
        Monitor monitor = this.getById(id);
        return monitor.getStreamLink();
    }

    @Override
    public Boolean updateMonitor(UpdateMonitorParam updateMonitorParam){
        Monitor monitor = new Monitor();
        monitor.setId(updateMonitorParam.getId());
        monitor.setName(updateMonitorParam.getName());
        monitor.setArea(updateMonitorParam.getArea());
        monitor.setLeader(updateMonitorParam.getLeader());
        monitor.setLatitude(updateMonitorParam.getLatitude());
        monitor.setLongitude(updateMonitorParam.getLongitude());
        monitor.setFall(updateMonitorParam.getFall());
        monitor.setFlame(updateMonitorParam.getFlame());
        monitor.setSmoke(updateMonitorParam.getSmoke());
        monitor.setSmoking(updateMonitorParam.getSmoking());
        monitor.setDangerArea(updateMonitorParam.getDangerArea());
        monitor.setStreamLink(updateMonitorParam.getIp());
        monitor.setLeftX(updateMonitorParam.getLeftX());
        monitor.setLeftY(updateMonitorParam.getLeftY());
        monitor.setRightX(updateMonitorParam.getRightX());
        monitor.setRightY(updateMonitorParam.getRightY());
        try{
            //TODO 向flask调用更新接口
            //更改Flask区域
            List<Integer> area = new ArrayList<>();
            area.add(updateMonitorParam.getLeftX());
            area.add(updateMonitorParam.getLeftY());
            area.add(updateMonitorParam.getRightX());
            area.add(updateMonitorParam.getRightY());
            List<Boolean> ability = new ArrayList<>();
            //0 火，1 抽烟，2跌倒，3挥拳，4挥手，5危险区域
            //TODO 改字段的时候要改这里
            ability.add(updateMonitorParam.getFlame());
            ability.add(updateMonitorParam.getSmoking());
            ability.add(updateMonitorParam.getFall());
            ability.add(updateMonitorParam.getDangerArea());
            ability.add(updateMonitorParam.getDangerArea());
            ability.add(updateMonitorParam.getDangerArea());
            if (!requestFlaskService.updateMonitorArea(updateMonitorParam.getIp(),area) && !requestFlaskService.updateMonitorAbility(updateMonitorParam.getIp(),ability)){
                return false;
            }
            updateById(monitor);
            return true;
        }catch (Exception e){
            log.error("更新监控失败");
            return false;
        }
    }
}
