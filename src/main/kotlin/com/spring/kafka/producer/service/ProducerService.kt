package com.spring.kafka.producer.service

import jakarta.annotation.PostConstruct
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProducerService(
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
                println("Thread: ${Thread.currentThread().name} Sending message $i")
                val result = exampleKafkaTemplate.sendDefault("$message $i").get()
                println("Thread: ${Thread.currentThread().name} Topic: ${result.recordMetadata.topic()}, Partition: ${result.recordMetadata.partition()}, Offset: ${result.recordMetadata.offset()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun Int.sendMessageComplete(message: String) {
        for (i in 1..this) {
            try {
                println("Thread: ${Thread.currentThread().name} Sending message $i")
                exampleKafkaTemplate.sendDefault("$message $i").whenComplete({ result, exception ->
                    if (exception != null) {
                        exception.printStackTrace()

                    } else {
                        println("Thread: ${Thread.currentThread().name} Topic: ${result.recordMetadata.topic()}, Partition: ${result.recordMetadata.partition()}, Offset: ${result.recordMetadata.offset()}")
                    }
                }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun Int.sendMessageCompleteAsync(message: String) {
        for (i in 1..this) {
            println("Thread: ${Thread.currentThread().name} Sending message $i")
            exampleKafkaTemplate.sendDefault("$message $i").whenCompleteAsync({ result, exception ->
                if (exception != null) {
                    exception.printStackTrace()
                } else {
                    println("Thread: ${Thread.currentThread().name} Topic: ${result.recordMetadata.topic()}, Partition: ${result.recordMetadata.partition()}, Offset: ${result.recordMetadata.offset()}")
                }
            }
            )
        }
    }
}