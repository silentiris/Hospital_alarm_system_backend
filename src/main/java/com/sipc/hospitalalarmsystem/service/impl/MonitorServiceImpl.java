package com.sipc.hospitalalarmsystem.service.impl;

import com.sipc.hospitalalarmsystem.dao.MonitorDao;
import com.sipc.hospitalalarmsystem.model.po.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {
    @Resource
    private MonitorDao monitorDao;

    @Override
    public List<Monitor> getMonitorList() {
        return monitorDao.selectList(null);
    }
}
