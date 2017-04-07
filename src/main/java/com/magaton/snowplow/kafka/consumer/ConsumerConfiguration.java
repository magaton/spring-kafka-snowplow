package com.magaton.snowplow.kafka.consumer;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import com.magaton.snowplow.kafka.serializer.ThriftDeserializer;
import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

@Configuration
@EnableKafka
public class ConsumerConfiguration {

  @Value("${kafka.servers.bootstrap}")
  private String bootstrapServers;
  

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ThriftDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "snowplow");
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");

    return props;
  }

  @Bean
  public ConsumerFactory<String, CollectorPayload> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
        new ThriftDeserializer());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CollectorPayload> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, CollectorPayload> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setBatchListener(true);
	factory.setConcurrency(4);
	factory.setConsumerFactory(consumerFactory());
	return factory;
  }

  @Bean
  public Consumer receiver() {
    return new Consumer();
  }
}
