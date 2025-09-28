package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class KafkaListenerService {
    @Autowired
    TradeService conversionService;
    @Autowired
    KafkaPublisher publisher;
    public static final Logger logger = Logger.getLogger(KafkaListenerService.class.getName());

    @KafkaListener(topics="instructions.inbound", groupId="mygroup")
    public void consumeTradeInstructions(String message) {
        try {
            List<CanonicalTrade> ct = conversionService.parseKafkaMessage(message);
            publisher.publishToTopic(ct);
            logger.info("Kafka message process successfully");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
