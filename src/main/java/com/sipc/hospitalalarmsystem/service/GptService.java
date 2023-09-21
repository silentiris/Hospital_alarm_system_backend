package com.sipc.hospitalalarmsystem.service;

import com.plexpt.chatgpt.entity.chat.Message;
import com.sipc.hospitalalarmsystem.model.dto.param.gpt.ChatParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 21:03
 */
public interface GptService {

    Message getText(ChatParam chatParam);
}
