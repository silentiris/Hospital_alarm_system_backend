package com.sipc.hospitalalarmsystem;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.Proxy;

@SpringBootTest
class HospitalAlarmSystemApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test1() {
//        Proxy proxy = Proxys.http("127.0.0.1", 1081);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);

    }

}
