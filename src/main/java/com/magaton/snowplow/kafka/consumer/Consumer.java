package com.magaton.snowplow.kafka.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

public class Consumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

  private ObjectMapper MAPPER = new ObjectMapper();
  
  private CountDownLatch latch = new CountDownLatch(1);

  @KafkaListener(topics = "${kafka.topic}")
  public void receive(List<CollectorPayload> events) {
    LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    try {
		LOGGER.info(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(events));
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}
