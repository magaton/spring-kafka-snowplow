package com.magaton.snowplow.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

public class Consumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

  private CountDownLatch latch = new CountDownLatch(1);

  @KafkaListener(topics = "${kafka.topic}")
  public void receive(CollectorPayload event) {
    LOGGER.info("received event collector {}; ip: {}; schema {}" , event.getCollector(), event.getIpAddress(), event.getSchema());
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}
