package com.sipc.hospitalalarmsystem.model.dto.param.gpt;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Data
public class ChatParam {

    List<String> defaultPrompt = Arrays.asList("我问你个问题，你告诉我答案。你是谁？", "你好，我是医患智警小智。我了解医院的日常挑战，特别是当涉及到病人和医务人员的安全时。无论你是护士、医生还是医院管理人员，我都在这里为你提供支持。你可以询问我关于智能视频监控系统的任何问题，包括如何配置预警、如何解读监控结果、如何采取措施等。或者，如果你只是需要关于如何更好地维护医院环境的建议，我也在这里为你提供帮助。请告诉我，我如何为你服务？\n");
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


