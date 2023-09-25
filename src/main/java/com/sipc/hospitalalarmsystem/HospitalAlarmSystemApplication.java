package com.sipc.hospitalalarmsystem;

import com.sipc.hospitalalarmsystem.util.RecordUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

import static com.sipc.hospitalalarmsystem.video.MonitorCommon.monitorMap;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class HospitalAlarmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAlarmSystemApplication.class, args);
    }
    //TODO: 从数据库中获取
    static {
        monitorMap.put(1,new RecordUtil("rtmp://101.43.254.115:1985/live/test"));
        for(Map.Entry<Integer,RecordUtil> entry:monitorMap.entrySet()){
            entry.getValue().start();
            System.err.println("monitor "+entry.getKey()+" start");
        }

    }

}
