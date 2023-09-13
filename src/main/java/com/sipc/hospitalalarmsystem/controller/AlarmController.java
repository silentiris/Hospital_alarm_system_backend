package com.sipc.hospitalalarmsystem.controller;

import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.alarm.UpdateAlarmParam;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.QueryAlarmListRes;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/api/v1/alarm")
public class AlarmController {

    @Autowired
    AlarmService alarmService;

    @PostMapping("/receive")
    public CommonResult<BlankRes> receiveAlarm(@RequestParam int cameraId, @RequestParam int caseType) {
        if (alarmService.receiveAlarm(cameraId, caseType))
            return CommonResult.success("接收成功");
        else
            return CommonResult.fail("接收失败");
    }

    @GetMapping("/{alarmId}")
    public CommonResult<GetAlarmRes> getAlarm(@PathVariable @NotNull(message = "alarmId不能为空") Integer alarmId) {
        GetAlarmRes alarm = alarmService.getAlarm(alarmId);
        if (alarm == null)
            return CommonResult.fail("查询失败");
        else
            return CommonResult.success(alarm);
    }

    @GetMapping("/query/cnt")
    public CommonResult<BlankRes> getAlarmCnt(@RequestParam(value = "caseType",required = false) Integer caseType,
                                              @RequestParam(value = "time1",required = false) String time1,
                                              @RequestParam(value = "time2",required = false) String time2) {
        Long alarmCnt = alarmService.getAlarmCnt(caseType, time1, time2);
        if (alarmCnt == null)
            return CommonResult.fail("查询失败");
        else
            return CommonResult.success(alarmCnt.toString());
    }

    @GetMapping("/query")
    public CommonResult<QueryAlarmListRes> queryAlarmList(@RequestParam(value = "pageNum",required = true)  Integer pageNum,
                                                          @RequestParam(value = "pageSize",required = true)  Integer pageSize,
                                                          @RequestParam(value = "caseType",required = false) Integer caseType,
                                                          @RequestParam(value = "status",required = false)  Integer status,
                                                          @RequestParam(value = "warningLevel",required = false)  Integer warningLevel,
                                                          @RequestParam(value = "processingContent",required = false)  String processingContent,
                                                          @RequestParam(value = "time1",required = false) String time1,
                                                          @RequestParam(value = "time2",required = false)  String time2) {

        List<Alarm> alarmList = alarmService.queryAlarmList(pageNum, pageSize, caseType, status, warningLevel, processingContent, time1, time2);

        if (alarmList == null)
            return CommonResult.fail("查询失败");

        QueryAlarmListRes queryAlarmListRes = new QueryAlarmListRes();
        queryAlarmListRes.setCount(alarmList.size());
        queryAlarmListRes.setAlarmList(alarmList);
        return CommonResult.success(queryAlarmListRes);
    }

    @PutMapping("/update")
    public CommonResult<BlankRes> updateAlarm(@Valid @RequestBody UpdateAlarmParam updateAlarmParam) {

        if (alarmService.updateAlarm(updateAlarmParam.getId(), updateAlarmParam.getStatus(), updateAlarmParam.getProcessingContent()))
            return CommonResult.success("更新成功");
        else
            return CommonResult.fail("更新失败");
    }

    @DeleteMapping("/{alarmId}")
    public CommonResult<BlankRes> deleteAlarm(@PathVariable @NotNull(message = "alarmId不能为空") Integer alarmId) {
        if (alarmService.deleteAlarm(alarmId))
            return CommonResult.success("删除成功");
        else
            return CommonResult.fail("删除失败");
    }
}
