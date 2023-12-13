package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.service.TestSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class TestSenderImpl implements TestSender {

    @Override
    public void sendMessage(String bootstrapServers, String topicName, Object message) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, message);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaProducer<String, Object> producer = new KafkaProducer<>(properties);
        producer.send(record);
        producer.close();
    }
}
