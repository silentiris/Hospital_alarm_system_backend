package com.sipc.hospitalalarmsystem.service.impl;

import com.sipc.hospitalalarmsystem.model.po.Case;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AlarmServiceImpl  implements AlarmService {

    @Override
    public void receiveAlarm(int id, int caseType) {
        log.info("receive alarm id: "+id+" caseType: "+caseType);
        Case case1 = new Case();





    }
}
