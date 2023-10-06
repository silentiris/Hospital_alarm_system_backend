package com.sipc.hospitalalarmsystem.model.dto.res.Monitor;

import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;
import lombok.Data;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 3:19
 */
@Data
public class GetMonitorsPosRes {

    @Data
    public static class MonitorPos{
        public MonitorPos(Monitor monitor){
            this.id = monitor.getId();
            this.latitude = monitor.getLatitude();
            this.longitude = monitor.getLongitude();
            this.name = monitor.getName();
            this.leader = monitor.getLeader();
            this.alarmCnt = monitor.getAlarmCnt();
            this.area = monitor.getArea();
            this.running = monitor.getRunning();
        }
        private Integer id;
        private String name;
        private String area;
        private String leader;
        private Integer alarmCnt;
        private Double latitude;
        private Double longitude;
        private Boolean running;
    }

    public GetMonitorsPosRes(List<Monitor> monitorList){
        this.total = monitorList.size();
        this.running = 0;
        this.monitorPosList = new java.util.ArrayList<>();
        for(Monitor monitor:monitorList){
            if(monitor.getRunning()){
                this.running++;
            }
            this.monitorPosList.add(new MonitorPos(monitor));
        }
    }

    private Integer total;
    private Integer running;
    private List<MonitorPos> monitorPosList;
}
