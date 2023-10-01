package com.sipc.hospitalalarmsystem.service.impl;

import com.sipc.hospitalalarmsystem.model.po.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import com.sipc.hospitalalarmsystem.util.RecordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.sipc.hospitalalarmsystem.video.MonitorCommon.monitorMap;

@Slf4j
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final MonitorService monitorService;
    @Autowired
    public CommandLineRunnerImpl(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Override
    public void run(String... args) {
//        List<Monitor> monitors = monitorService.getMonitorList();
//        for(Monitor monitor : monitors){
//            monitorMap.put(monitor.getId(),new RecordUtil(monitor.getStream()));
//        }
//        for (Map.Entry<Integer, RecordUtil> entry : monitorMap.entrySet()) {
//            entry.getValue().start();
//            log.info("monitor " + entry.getKey() + " start");
//        }
    }
}