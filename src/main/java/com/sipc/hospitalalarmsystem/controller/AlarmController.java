package com.sipc.hospitalalarmsystem.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.alarm.UpdateAlarmParam;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.*;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import com.sipc.hospitalalarmsystem.util.HttpUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/alarm")
public class AlarmController {

    @Autowired
    AlarmService alarmService;
    @PostMapping("/receive")
    public CommonResult<BlankRes> receiveAlarm(@RequestParam(value = "cameraId",required = true) int cameraId,
                                               @RequestParam(value = "caseType",required = true) int caseType,
                                               @RequestParam(value = "clipId",required = true) String clipId) {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("cid",""); //cid不填默认全发
        paramMap.put("title","报警通知");
        paramMap.put("content","您有一条新的报警信息，请及时处理");
        Map<String,String> optMap = new HashMap<>();
        Map<String,String> catMap = new HashMap<>();
        catMap.put("/message/android/category","WORK");
        optMap.put("HW", catMap.toString());
        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("date1",1);
        dateMap.put("date2",1);
        paramMap.put("options",optMap.toString());
        paramMap.put("date",dateMap.toString());
        Random random = new Random();
        String randomString = random.ints(10, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
        paramMap.put("request_id",randomString);
        try {
            String resJson = HttpUtils.postJson("https://fc-mp-e0386718-0219-4138-80a9-902540e76f67.next.bspapp.com/notice", new ObjectMapper().writeValueAsString(paramMap));
            if(resJson.contains("success")){
                log.info("发送报警通知成功");
            }else {
                log.error("发送报警通知失败");
            }
        } catch (JsonProcessingException e) {
            log.error("发送报警通知失败");
        }
        if (alarmService.receiveAlarm(cameraId, caseType,clipId))
            return CommonResult.success("接收成功");
        else
            return CommonResult.fail("接收失败");
    }

    @GetMapping("/{alarmId}")
    public CommonResult<GetAlarmRes> getAlarm(@PathVariable @NotNull(message = "alarmId不能为空") Integer alarmId) {
        SqlGetAlarm alarm = alarmService.getAlarm(alarmId);
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
        List<TimePeriod> g3;
        LocalDateTime currentDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).minusDays(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = currentDate.format(formatter);
        if (defer == 1){
            g1 = alarmService.getDayHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getDayAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = alarmService.SqlGetCaseTypesDayHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
        }
        else if (defer == 3){
            g1 = alarmService.getThreeDaysHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getThreeDaysAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = alarmService.SqlGetCaseTypesThreeDaysHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
        }
        else if (defer == 7){
            g1 = alarmService.getWeekHistoryCnt(time);
            getHistoryCntRes.setGraph1(g1);
            g2 = alarmService.getWeekAreasHistoryCnt(time);
            getHistoryCntRes.setGraph2(g2);
            g3 = alarmService.SqlGetCaseTypesWeekHistoryCnt(time);
            getHistoryCntRes.setGraph3(g3);
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

        List<SqlGetAlarm> alarmList = alarmService.queryAlarmList(pageNum, pageSize, caseType, status, warningLevel, time1, time2);

        if (alarmList == null)
            return CommonResult.fail("查询失败");

        List<GetAlarmRes> res = new ArrayList<>();

        for(SqlGetAlarm alarm : alarmList){
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
                    List<SqlGetAlarm> alarmRes =  alarmService.queryAlarmList(1, 5000, null, null, null, null, null);
                    List<GetAlarmRes> res = new ArrayList<>();
                    for (SqlGetAlarm alarmRe : alarmRes) {
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

    static public GetAlarmRes SqlGetAlarmRes2GetAlarmRes(SqlGetAlarm sqlGetAlarm){
        GetAlarmRes getAlarmRes = new GetAlarmRes();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        getAlarmRes.setId(sqlGetAlarm.getId());
        getAlarmRes.setName(sqlGetAlarm.getName());
        getAlarmRes.setEventName(sqlGetAlarm.getCaseTypeName());
        getAlarmRes.setLevel(sqlGetAlarm.getWarningLevel());
        getAlarmRes.setDate(sdf.format(sqlGetAlarm.getCreateTime()));
        getAlarmRes.setDepartment(sqlGetAlarm.getArea());
        getAlarmRes.setDeal(sqlGetAlarm.getStatus() ? "已处理":"未处理");
        getAlarmRes.setContent(sqlGetAlarm.getProcessingContent());
        getAlarmRes.setVideo(sqlGetAlarm.getClipLink());
        return getAlarmRes;
    }
}
