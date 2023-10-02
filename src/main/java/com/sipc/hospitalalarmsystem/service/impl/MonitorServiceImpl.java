package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.MonitorDao;
import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorDao,Monitor> implements MonitorService {


    @Override
    public List<Monitor> getMonitorList() {
        return this.list();
    }
}
