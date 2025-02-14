package hu.kibit.assignment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring application configuration class.
 */
@Configuration
public class AppConfiguration {
    /** {@link KafkaProperties} bean. */
    @Autowired
    private KafkaProperties kafkaProperties;
    /** Name of the Kafka topic for notifications. */
    @Value("${kafka.notification.topic.name}")
    private String notificationTopicName;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates properties {@link Map} for Kafka producer.
     * @return Map of properties
     */
    private Map<String, Object> kafkaProducerConfigurationMap() {
        final Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    /**
     * Creates Kafka {@link ProducerFactory} bean.
     * @return New ProducerFactory instance
     */
    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigurationMap());
    }

    /**
     * Creates a {@link KafkaTemplate} bean.
     * @return New {@link KafkaTemplate} instance
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(final ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Creates a new Kafka topic for the notifications.
     * @return Topic instance
     */
    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(notificationTopicName, 3, (short) 1);
    }
}
