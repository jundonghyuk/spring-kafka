package com.spring.kafka.producer.fireforgot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
data class KafkaProperties(
    val bootstrapServers: List<String>,
    val example: Producer
) {

    data class Producer(
        val topic: String
    )
}