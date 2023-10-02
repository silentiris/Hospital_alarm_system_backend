package com.sipc.hospitalalarmsystem.service;


import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;

import java.util.List;

public interface MonitorService {
    List<Monitor> getMonitorList();
}
