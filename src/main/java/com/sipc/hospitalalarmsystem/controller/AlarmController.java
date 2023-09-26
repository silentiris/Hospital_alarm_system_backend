package com.sipc.hospitalalarmsystem.controller;

import com.alibaba.excel.EasyExcel;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.alarm.UpdateAlarmParam;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.*;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Validated
@Slf4j
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
        SqlGetAlarmRes alarm = alarmService.getAlarm(alarmId);
        if (alarm == null)
            return CommonResult.fail("查询失败");

        return CommonResult.success(SqlGetAlarmRes2GetAlarmRes(alarm));
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

    @GetMapping("/query/cnt/history")
    public CommonResult<GetHistoryCntRes> getHistoryCntRes(@RequestParam(value = "defer") Integer defer) {
        GetHistoryCntRes getHistoryCntRes = new GetHistoryCntRes();
        List<TimePeriod> g1;
        List<TimePeriod> g2;
        LocalDateTime currentDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).minusDays(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = currentDate.format(formatter);
        if (defer == 1){
            g1 = alarmService.getDayHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getDayAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
        }
        else if (defer == 3){
            g1 = alarmService.getThreeDaysHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getThreeDaysAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
        }
        else if (defer == 7){
            g1 = alarmService.getWeekHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getWeekAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
        }
        else{
            return CommonResult.fail("参数错误");
        }

        return CommonResult.success(getHistoryCntRes);
    }


    @GetMapping("/query")
    public CommonResult<QueryAlarmListRes> queryAlarmList(@RequestParam(value = "pageNum",required = true)  Integer pageNum,
                                                          @RequestParam(value = "pageSize",required = true)  Integer pageSize,
                                                          @RequestParam(value = "caseType",required = false) Integer caseType,
                                                          @RequestParam(value = "status",required = false)  Integer status,
                                                          @RequestParam(value = "warningLevel",required = false)  Integer warningLevel,
                                                          @RequestParam(value = "time1",required = false) String time1,
                                                          @RequestParam(value = "time2",required = false)  String time2) {

        List<SqlGetAlarmRes> alarmList = alarmService.queryAlarmList(pageNum, pageSize, caseType, status, warningLevel, time1, time2);

        if (alarmList == null)
            return CommonResult.fail("查询失败");

        List<GetAlarmRes> res = new ArrayList<>();

        for(SqlGetAlarmRes alarm : alarmList){
            res.add(SqlGetAlarmRes2GetAlarmRes(alarm));
        }
        QueryAlarmListRes queryAlarmListRes = new QueryAlarmListRes();
        queryAlarmListRes.setCount(res.size());
        queryAlarmListRes.setAlarmList(res);
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

    @GetMapping("/realtime")
    public CommonResult<RealTimeAlarmRes> getRealTimeAlarm() {
        RealTimeAlarmRes realTimeAlarmRes = alarmService.getRealTimeAlarmRes();
        if (realTimeAlarmRes == null)
            return CommonResult.fail("查询失败");
        else
            return CommonResult.success(realTimeAlarmRes);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAlarms(){
        String Filename = "temp.xlsx";
        File file = new File(Filename);
        EasyExcel.write(Filename, GetAlarmRes.class)
                .sheet("报警数据")
                .doWrite(() -> {
                    List<SqlGetAlarmRes> alarmRes =  alarmService.queryAlarmList(1, 5000, null, null, null, null, null);
                    List<GetAlarmRes> res = new ArrayList<>();
                    for (SqlGetAlarmRes alarmRe : alarmRes) {
                        res.add(SqlGetAlarmRes2GetAlarmRes(alarmRe));
                    }
                    return res;
                });
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        }catch (Exception e) {
            log.error("文件读取失败");
            return null;
        }
        return ResponseEntity.ok().body(bytes);
        }

    static public GetAlarmRes SqlGetAlarmRes2GetAlarmRes(SqlGetAlarmRes sqlGetAlarmRes){
        GetAlarmRes getAlarmRes = new GetAlarmRes();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        getAlarmRes.setId(sqlGetAlarmRes.getId());
        getAlarmRes.setName(sqlGetAlarmRes.getName());
        getAlarmRes.setEventName(sqlGetAlarmRes.getCaseType());
        getAlarmRes.setLevel(sqlGetAlarmRes.getWarningLevel());
        getAlarmRes.setDate(sdf.format(sqlGetAlarmRes.getCreateTime()));
        getAlarmRes.setDepartment(sqlGetAlarmRes.getArea());
        getAlarmRes.setDeal(sqlGetAlarmRes.getStatus() ? "已处理":"未处理");
        getAlarmRes.setContent(sqlGetAlarmRes.getProcessingContent());
        getAlarmRes.setVideo(sqlGetAlarmRes.getClipLink());
        return getAlarmRes;
    }
}
