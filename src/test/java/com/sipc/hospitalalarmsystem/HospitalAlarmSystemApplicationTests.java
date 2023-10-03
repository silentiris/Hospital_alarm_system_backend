package com.sipc.hospitalalarmsystem;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.dto.res.FlaskResponse.updateMonitorAreaRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarm;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import com.sipc.hospitalalarmsystem.service.RecordService;
import com.plexpt.chatgpt.ChatGPT;


import com.sipc.hospitalalarmsystem.util.HttpUtils;
import com.sipc.hospitalalarmsystem.util.JacksonUtils;
import com.sipc.hospitalalarmsystem.util.JwtUtils;
import com.sipc.hospitalalarmsystem.util.OssUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static com.sipc.hospitalalarmsystem.controller.AlarmController.SqlGetAlarmRes2GetAlarmRes;


@SpringBootTest
class HospitalAlarmSystemApplicationTests {
    @Autowired
    private RecordService recordService;
    @Autowired
    AlarmService alarmService;
    @Autowired
    private OssUtil ossUtil;

    @Test
    void contextLoads() throws InterruptedException, JsonProcessingException {
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
        System.out.println(HttpUtils.postJson("https://fc-mp-e0386718-0219-4138-80a9-902540e76f67.next.bspapp.com/notice", new ObjectMapper().writeValueAsString(paramMap)));
    }


}
