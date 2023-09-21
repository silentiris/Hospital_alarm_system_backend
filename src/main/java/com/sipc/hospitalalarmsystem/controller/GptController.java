package com.sipc.hospitalalarmsystem.controller;

import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.gpt.ChatParam;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.service.GptService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 20:59
 */

@Validated
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/api/v1/gpt")
public class GptController {

    @Autowired
    GptService gptService;

    @PostMapping()
    public CommonResult<BlankRes> chat(@Valid  @RequestBody ChatParam param,
                                       @RequestParam(required = false) String balance) {


        log.info("正在提问: " + param.getMessage());
        Message message = gptService.getText(param);
        String text = message.getContent();


        log.info("问题：" + param.getMessage() + "\n回答：" + text);

        return CommonResult.success(text);
    }
}
