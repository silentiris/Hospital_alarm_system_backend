package com.sipc.hospitalalarmsystem.service.impl.GptServiceImpl;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.sipc.hospitalalarmsystem.model.dto.param.gpt.ChatParam;
import com.sipc.hospitalalarmsystem.service.GptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 21:08
 */

@Slf4j
@Service
public class GptServiceImpl implements GptService {

    private static Map<String, List<Message>> context = new HashMap<>();
    String APIKEY = "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";

    public List<Message> get(String id) {
        List<Message> messages = context.get(id);
        if (messages == null) {
            messages = new ArrayList<>();
            context.put(id, messages);
        }

        return messages;
    }

    public void add(String id, Message message) {
        List<Message> messages = context.get(id);
        if (messages == null) {
            messages = new ArrayList<>();
            context.put(id, messages);
        }

        messages.add(message);
    }

    @Override
    public Message getText(ChatParam param) {


        String prompt = param.buildPrompt();


        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(APIKEY)
                .timeout(50)
                .build()
                .init();

        try {
            Message message = Message.of(prompt);

            List<Message> messages = get(param.getId());
            messages.add(message);

            ChatCompletionResponse completion = chatGPT.chatCompletion(messages);
            Message message1 = completion.getChoices().get(0).getMessage();


            add(param.getId(), message);
            add(param.getId(), message1);

            return message1;

        } catch (Exception e) {
            log.error("API调用出错：{}", e);
            throw new RuntimeException("服务器挤爆了，请检查KEY， 网络。请输入你的APIKEY后试用: " + e.getMessage());
        }
    }

}
