package com.spring.kafka.producer.fireforgot

import jakarta.annotation.PostConstruct
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService(
    val exampleKafkaTemplate: KafkaTemplate<String, String>,
) {

    @PostConstruct
    fun init() {
        10.sendMessage("Hello Kafka")
    }

    private fun Int.sendMessage(message: String) {

        for (i in 1..this) {
            println("Sending message $i")
            exampleKafkaTemplate.sendDefault("$message $i");
        }
    }
}