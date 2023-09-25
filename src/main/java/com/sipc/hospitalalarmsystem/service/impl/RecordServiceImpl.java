package com.sipc.hospitalalarmsystem.service.impl;

import com.sipc.hospitalalarmsystem.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sipc.hospitalalarmsystem.video.MonitorCommon.monitorMap;

@Service
@Slf4j
public class RecordServiceImpl implements RecordService {
    @Override
    public String getRecord( int monitorId) {
        return monitorMap.get(monitorId).getClipLink();
    }
}
