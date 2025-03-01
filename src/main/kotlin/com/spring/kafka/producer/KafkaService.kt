package com.spring.kafka.producer

import jakarta.annotation.PostConstruct
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService(
    val exampleKafkaTemplate: KafkaTemplate<String, String>,
) {

    @PostConstruct
    fun init() {
        5.sendMessageSync("Hello Kafka")
    }

    private fun Int.sendMessageFireForgot(message: String) {

        for (i in 1..this) {
            println("Sending message $i")
            exampleKafkaTemplate.sendDefault("$message $i");
        }
    }

    private fun Int.sendMessageSync(message: String) {
        for (i in 1..this) {
            try {
                println("Sending message $i")
                val result = exampleKafkaTemplate.sendDefault("$message $i").get()
                println("Topic: ${result.recordMetadata.topic()}, Partition: ${result.recordMetadata.partition()}, Offset: ${result.recordMetadata.offset()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}