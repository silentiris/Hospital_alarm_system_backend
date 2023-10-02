package com.sipc.hospitalalarmsystem;

import com.alibaba.excel.EasyExcel;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.GetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarmRes;
import com.sipc.hospitalalarmsystem.service.AlarmService;
import com.sipc.hospitalalarmsystem.service.RecordService;
import com.plexpt.chatgpt.ChatGPT;


import com.sipc.hospitalalarmsystem.util.OssUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
    void contextLoads() throws InterruptedException {
        System.out.println(ossUtil.getClipLinkByUuid("51987bdb-1ab0-4e61-8fc1-9257de192a7b"));
    }

    @Test
    void test1() {
//        Proxy proxy = Proxys.http("127.0.0.1", 1081);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("")
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);

    }

    @Test
    void ExcelTest() {

        String fileName = "test.xlsx";
        EasyExcel.write(fileName, GetAlarmRes.class)
                .sheet("报警数据")
                .doWrite(() -> {
                    List<SqlGetAlarmRes> alarmRes =  alarmService.queryAlarmList(1, 5000, null, null, null, null, null);
                    List<GetAlarmRes> res = new ArrayList<>();
                    for (SqlGetAlarmRes alarmRe : alarmRes) {
                        res.add(SqlGetAlarmRes2GetAlarmRes(alarmRe));
                    }
                    return res;
                });
    }

}
