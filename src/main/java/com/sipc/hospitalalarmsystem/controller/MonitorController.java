package com.sipc.hospitalalarmsystem.controller;

import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.res.Monitor.GetMonitorRes;
import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-01 20:50
 */

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/monitor")
public class MonitorController {

    @Autowired
    MonitorService monitorService;

    @GetMapping()
    public CommonResult<List<GetMonitorRes>> getMonitorList(){

        List<Monitor> sqlMonitors = monitorService.getMonitorList();
        if (sqlMonitors == null) {
            return CommonResult.fail("获取监控列表失败");
        }
        List<GetMonitorRes> getMonitorResList = new ArrayList<>();
        for (Monitor monitor : sqlMonitors) {
            GetMonitorRes getMonitorRes = new GetMonitorRes();
            GetMonitorRes.MonitorBorder monitorBorder = new GetMonitorRes.MonitorBorder();
            monitorBorder.setLeftX(monitor.getLeftX());
            monitorBorder.setLeftY(monitor.getLeftY());
            monitorBorder.setRightX(monitor.getRightX());
            monitorBorder.setRightY(monitor.getRightY());
//            GetMonitorRes.MonitorAbilities monitorAbilities = new GetMonitorRes.MonitorAbilities();



            getMonitorRes.setId(monitor.getId());
            getMonitorRes.setName(monitor.getName());
            getMonitorRes.setDeal(monitor.getRunning()?"正在运行":"停止");
            getMonitorRes.setDepartment(monitor.getArea());
            getMonitorRes.setLeader(monitor.getLeader());
            getMonitorRes.setRunning(monitor.getRunning());

        }



        return CommonResult.success(getMonitorResList);
    }

}
