package com.messaging.jms.sample.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.jms.sample.config.JmsConfig;
import com.messaging.jms.sample.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;


    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        System.out.println("I am Sending a message");
        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello Vinit")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

        System.out.println("Message Sent");
    }


    @Scheduled(fixedRate = 2000)
    public void sendandRecieveMessage() throws JMSException {

        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message recievedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            Message helloMessage = null;
            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                throw new JMSException("ohho");
            }
            helloMessage.setStringProperty("_type","com.messaging.jms.sample.model.HelloWorldMessage");
            return helloMessage;

        });
        System.out.println(recievedMessage.getBody(String.class));
    }
}
