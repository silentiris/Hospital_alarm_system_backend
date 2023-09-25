package com.sipc.hospitalalarmsystem;

import com.sipc.hospitalalarmsystem.service.RecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HospitalAlarmSystemApplicationTests {
    @Autowired
    private RecordService recordService;
    @Test
    void contextLoads() throws InterruptedException {
        Thread.sleep(10*1000);
        System.out.println(recordService.getRecord(1));
    }

}
