package com.magaton.snowplow.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import com.magaton.snowplow.kafka.consumer.Consumer;
import com.magaton.snowplow.kafka.producer.Producer;
import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTest {

  @Autowired
  private Producer sender;

  @Autowired
  private Consumer consumer;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "snowplow-data");

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    System.setProperty("kafka.servers.bootstrap", embeddedKafka.getBrokersAsString());
  }

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    // wait until the partitions are assigned
    for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
        .getListenerContainers()) {
      if (messageListenerContainer instanceof ConcurrentMessageListenerContainer) {
        ConcurrentMessageListenerContainer<String, CollectorPayload> concurrentMessageListenerContainer =
            (ConcurrentMessageListenerContainer<String, CollectorPayload>) messageListenerContainer;

        ContainerTestUtils.waitForAssignment(concurrentMessageListenerContainer,
            embeddedKafka.getPartitionsPerTopic());
      }
    }
  }

  @Test
  public void testConsumer() throws Exception {
    CollectorPayload collectorPayload = new CollectorPayload();
    collectorPayload.setCollector("dummy-collector");    
    sender.send(collectorPayload);
    consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(consumer.getLatch().getCount()).isEqualTo(0);
  }
}
