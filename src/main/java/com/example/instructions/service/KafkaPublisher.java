package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class KafkaPublisher {
    public static final String topic = "instructions.outbound";
    public static final Logger logger = Logger.getLogger(KafkaPublisher.class.getName());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemp;

    public void publishToTopic(List<CanonicalTrade> canonicalTradesrades)
    {
        logger.info("Publishing to topic "+topic);
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (CanonicalTrade trade : canonicalTradesrades) {
                String jsonString = mapper.writeValueAsString(trade);
                logger.info(jsonString);
                this.kafkaTemp.send(topic, jsonString);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
