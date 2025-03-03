package com.spring.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
data class KafkaProperties(
    val bootstrapServers: List<String>,
    val exampleProducer: Producer,
    val exampleConsumer: Consumer
) {

    data class Producer(
        val topic: String
    )

    data class Consumer(
        val groupId: String,
        val enableAutoCommit: Boolean,
        val autoOffsetReset: String,
        val topic: String
    )
}