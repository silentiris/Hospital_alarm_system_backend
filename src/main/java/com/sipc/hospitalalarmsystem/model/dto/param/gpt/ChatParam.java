package com.sipc.hospitalalarmsystem.model.dto.param.gpt;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Data
public class ChatParam {

    List<String> defaultPrompt = Arrays.asList("我问你个问题，你告诉我答案。你是谁？", "我是医患智警，一个专门为实习医生提供建议和帮助的智能助理。" +
            "我被设计用于回答各种问题和提供各种帮助。您有什么需要我帮忙的吗？");
    public static String Question = "Q:\n";
    public static String AI = "A:\n";
    String id;
    String message;
    List<List<String>> context;

    public String buildPrompt() {
        if (CollectionUtils.isEmpty(context)) {
            context = Arrays.asList(defaultPrompt);
        }
        String result = "";

        for (List<String> pro : context) {
            result = result + Question + pro.get(0) + "\n";
            result = result + AI + pro.get(1) + "\n";
        }

        result = result + Question + message + "\n" + AI;

        return result;
    }

}


