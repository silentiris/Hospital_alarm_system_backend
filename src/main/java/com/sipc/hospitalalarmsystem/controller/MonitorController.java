package com.sipc.hospitalalarmsystem.controller;

import com.sipc.hospitalalarmsystem.aop.Pass;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.Monitor.CreateMonitorParam;
import com.sipc.hospitalalarmsystem.model.dto.param.Monitor.UpdateMonitorParam;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Monitor.CreateMonitorRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Monitor.GetMonitorsPosRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Monitor.GetMonitorListRes;
import com.sipc.hospitalalarmsystem.model.po.Monitor.Monitor;
import com.sipc.hospitalalarmsystem.service.MonitorService;
import com.sipc.hospitalalarmsystem.util.JwtUtils;
import com.sipc.hospitalalarmsystem.util.TokenThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<List<GetMonitorListRes>> getMonitorList(){

        List<Monitor> sqlMonitors = monitorService.getMonitorList();
        if (sqlMonitors == null) {
            return CommonResult.fail("获取监控列表失败");
        }
        List<GetMonitorListRes> getMonitorListResList = new ArrayList<>();
        for (Monitor monitor : sqlMonitors) {
            GetMonitorListRes getMonitorListRes = new GetMonitorListRes(monitor);
            getMonitorListResList.add(getMonitorListRes);
        }

        return CommonResult.success(getMonitorListResList);
    }

    @GetMapping("/map")
    public CommonResult<GetMonitorsPosRes> getMonitorsPos(){
        List<Monitor> sqlMonitors = monitorService.getMonitorList();
        if (sqlMonitors == null) {
            return CommonResult.fail("获取监控列表失败");
        }
        GetMonitorsPosRes getMonitorsPosRes = new GetMonitorsPosRes(sqlMonitors);

        return CommonResult.success(getMonitorsPosRes);
    }

    @GetMapping("/flask/info")
    public CommonResult<Monitor> getMonitor(){
        Monitor monitor = JwtUtils.getMonitorByToken(TokenThreadLocalUtil.getInstance().getToken());
        monitor = monitorService.getMonitorById(monitor.getId());
        if (monitor == null) {
            return CommonResult.fail("token错误");
        }

        return CommonResult.success(monitor);
    }

    @PostMapping("/update")
    public CommonResult<BlankRes> updateMonitor(@Valid @RequestBody UpdateMonitorParam updateMonitorParam){
        if (!monitorService.updateMonitor(updateMonitorParam)){
            return CommonResult.fail("更新失败");
        }
        return CommonResult.success("更新成功");
    }

    @PostMapping("/flask/create")
    @Pass
    public CommonResult<CreateMonitorRes> createMonitor(@Valid @RequestBody CreateMonitorParam createMonitorParam){
            CreateMonitorRes createMonitorRes = new CreateMonitorRes();
            Integer id = monitorService.createMonitor(createMonitorParam);
            if (id==-1){
                return CommonResult.fail("创建失败");
            }
            createMonitorRes.setId(id);
            createMonitorRes.setToken(JwtUtils.signMonitor(id));

            return CommonResult.success(createMonitorRes);
    }

    @GetMapping("/image/{monitorId}")
    public CommonResult<String> getMonitorImg(@PathVariable @NotNull Integer monitorId){
        String img = monitorService.getMonitorImg(monitorId);
        if (img == null) {
            return CommonResult.fail("获取图片失败");
        }
        return CommonResult.success(img);
    }

    @PostMapping("/switch/{id}")
    public CommonResult<BlankRes> switchMonitor(@PathVariable @NotNull Integer id){
        if (!monitorService.switchMonitor(id)){
            return CommonResult.fail("开启或关闭失败");
        }
        return CommonResult.success("开启或关闭成功");
    }
}
