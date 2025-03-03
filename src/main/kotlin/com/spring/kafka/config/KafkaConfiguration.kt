package com.spring.kafka.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties

@EnableConfigurationProperties(KafkaProperties::class)
@Configuration
class KafkaConfiguration(
    val kafkaProperties: KafkaProperties,
) {

    @Bean
    fun exampleKafkaTemplate(): KafkaTemplate<String, String> {
        val kafkaTemplate = KafkaTemplate(ProducerFactory())
        kafkaTemplate.defaultTopic = kafkaProperties.exampleProducer.topic
        return kafkaTemplate
    }

    @Bean
    fun ProducerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    private fun producerConfigs(): Map<String, Any?> {
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        )
    }

    @Bean
    fun autoCommitConsumerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(autoCommitConsumerConfigs())
        factory.consumerFactory = consumerFactory
        factory.containerProperties.idleBetweenPolls = 1000
        return factory
    }

    private fun autoCommitConsumerConfigs(): Map<String, Any?> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.exampleConsumer.groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.exampleConsumer.autoOffsetReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to kafkaProperties.exampleConsumer.enableAutoCommit,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        )
    }

    @Bean
    fun manualCommitSyncConsumerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(manualCommitSyncConsumerConfigs())
        factory.consumerFactory = consumerFactory
        /**
         * 수동 커밋 모드로 동작하려면 해당 설정이 필요함
         */
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
//        factory.containerProperties.idleBetweenPolls = 1000
        return factory
    }

    private fun manualCommitSyncConsumerConfigs(): Map<String, Any?> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.exampleConsumer.groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.exampleConsumer.autoOffsetReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        )
    }

    @Bean
    fun manualCommitAsyncConsumerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(manualCommitAsyncConsumerConfigs())
        factory.consumerFactory = consumerFactory
        /**
         * 수동 커밋 모드로 동작하려면 해당 설정이 필요함
         */
        factory.containerProperties.isSyncCommits = false
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        factory.containerProperties.isAsyncAcks = true
//        factory.containerProperties.idleBetweenPolls = 1000
        return factory
    }

    private fun manualCommitAsyncConsumerConfigs(): Map<String, Any?> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.exampleConsumer.groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.exampleConsumer.autoOffsetReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        )
    }
}