package com.magaton.snowplow.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

public class Producer {

  @Value("${kafka.topic}")
  private String topic;

  private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

  @Autowired
  private KafkaTemplate<String, CollectorPayload> kafkaTemplate;

  public void send(CollectorPayload collectorPayload) {
    LOGGER.info("sending collectorPayload='{}'", collectorPayload.getCollector());
    kafkaTemplate.send(topic, collectorPayload);
  }
}
