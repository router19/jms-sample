package com.messaging.jms.sample.listener;

import com.messaging.jms.sample.config.JmsConfig;
import com.messaging.jms.sample.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE, concurrency = "4-10")
    public void handleMessage(@Payload HelloWorldMessage helloWorldMessage,
                              @Headers MessageHeaders headers, Message message) {
//        System.out.println(" I got a message");
//        System.out.println(helloWorldMessage);
//        throw new RuntimeException("Fatal Error");
    }

    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE, concurrency = "4-10")
    public void handleMessageForReceiveandSend(@Payload HelloWorldMessage helloWorldMessage,
                              @Headers MessageHeaders headers, Message message) throws JMSException {
        HelloWorldMessage payloadMesg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("World")
                .build();
        jmsTemplate.convertAndSend(message.getJMSReplyTo(),payloadMesg);
    }

}
