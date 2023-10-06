package com.sipc.hospitalalarmsystem.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.sipc.hospitalalarmsystem.model.dto.param.gpt.ChatParam;
import com.sipc.hospitalalarmsystem.service.impl.GPTEventSourceListener;
import com.sipc.hospitalalarmsystem.util.KeyUtils.KeyManager;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-04 14:28
 */

@Slf4j
@Service
@ServerEndpoint(value = "/api/v1/gpt/ws/{token}")
public class WebsocketUtil {

    private static final int MAX_RETRIES = 3;

    private static Map<String, Integer> retryCounts = new HashMap<>();

    public static Map<String,String> localMessage = new HashMap<>();

    private static ConcurrentHashMap<String, WebsocketUtil> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    @Setter
    private Consumer<String> onComplete = s -> {};
    private static Map<String, List<Message>> context = new HashMap<>();
    private static KeyManager keyManager;
    static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
    @Autowired
    public void setInstantKeyManager(KeyManager keyManager) {
        WebsocketUtil.keyManager = keyManager;
    }

    @OnOpen
    public void onOpen(@PathParam("token") String token,Session session) {
        if ((!StringUtils.hasLength(token)) || (!JwtUtils.verify(token))) {
            log.error("WebSocket token错误!");
            throw new RuntimeException("鉴权异常");
        }
        this.session = session;
        webSocketMap.put(session.getId(), this);
        log.info("WebSocket opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        log.info("WebSocket received message: " + message);
        //存储发来的消息
        localMessage.put(session.getId(),message);
        requestAndSendData(message, session);
    }

    public static void requestAndSendData(String message, Session session) throws IOException {

        int currentRetry = retryCounts.getOrDefault(session.getId(), 0);

        if (currentRetry >= MAX_RETRIES) {
            log.error("Max retries reached for session: " + session.getId());
            retryCounts.remove(session.getId());
            session.getBasicRemote().sendText("Max retries reached");
            return;
        }


        ChatParam param = new ChatParam();
        param.setMessage(message);
        param.setId(session.getId());
        String prompt = param.buildPrompt();
        String APIKEY = keyManager.getKey();
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(50)
                .apiKey(APIKEY)
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        GPTEventSourceListener listener = new GPTEventSourceListener(session);
        Message message2 = Message.of(prompt);

        List<Message> messages = get(session.getId());
        messages.add(message2);
        listener.setOnComplate(msg -> {
            add(session.getId(), message2);
            add(session.getId(), Message.ofAssistant(msg));
        });
        chatGPTStream.streamChatCompletion(messages, listener);

        log.info("开始回复："+ session.getId());
    }

    @OnError
    public void onError(Throwable t) {
        log.error("WebSocket error: " + t.getMessage(), t);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        log.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    public static void clearContext(String id) {
        context.remove(id);
    }

    public static List<Message> get(String id) {
        List<Message> messages = context.get(id);
        if (messages == null) {
            messages = new ArrayList<>();
            context.put(id, messages);
        }

        return messages;
    }

    public static void add(String id, Message message) {
        List<Message> messages = context.get(id);
        if (messages == null) {
            messages = new ArrayList<>();
            context.put(id, messages);
        }

        messages.add(message);
    }

    public static void retryCountPlus(Session session){
        retryCounts.put(session.getId(), retryCounts.getOrDefault(session.getId(), 0) + 1);
    }
}

