package com.spring.kafka.consumer.service

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class ConsumerService {

    @KafkaListener(
        topics = ["#{'\${kafka.example-consumer.topic}'}"],
        groupId = "#{'\${kafka.example-consumer.group-id}'}",
        containerFactory = "autoCommitConsumerFactory"
    )
    fun consumeAuto(message: String) {
        consumeAutoCommit(message)
    }

    private fun consumeAutoCommit(message: String) {
        println("Consumed message: $message")
    }

    @KafkaListener(
        topics = ["#{'\${kafka.example-consumer.topic}'}"],
        groupId = "#{'\${kafka.example-consumer.group-id}'}",
        containerFactory = "manualCommitSyncConsumerFactory"
    )
    fun consumeManualSync(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        consumeManualCommitSync(record, acknowledgment)
    }

    private fun consumeManualCommitSync(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        println("Topic: ${record.topic()} Key: ${record.key()}, Value: ${record.value()}, Partition: ${record.partition()}, Offset: ${record.offset()}")
        println("Befor acknowledgment")
        acknowledgment.acknowledge()
        println("After acknowledgment")
        Thread.sleep(5000) // 지연 작업 추가
    }

    @KafkaListener(
        topics = ["#{'\${kafka.example-consumer.topic}'}"],
        groupId = "#{'\${kafka.example-consumer.group-id}'}",
        containerFactory = "manualCommitAsyncConsumerFactory"
    )
    fun consumeManualAsync(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        consumeManualCommitAsync(record, acknowledgment)
    }

    private fun consumeManualCommitAsync(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        println("Before acknowledgment")
        println("Topic: ${record.topic()} Key: ${record.key()}, Value: ${record.value()}, Partition: ${record.partition()}, Offset: ${record.offset()}")
        acknowledgment.acknowledge()
        println("After acknowledgment")
        Thread.sleep(5000) // 지연 작업 추가
    }
}