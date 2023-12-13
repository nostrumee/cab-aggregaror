package com.modsen.passengerservice.service;

public interface TestSender {
    void sendMessage(String bootstrapServers, String topicName, Object message);
}
