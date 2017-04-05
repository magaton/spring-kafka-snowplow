package com.magaton.snowplow.kafka.serializer;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import com.magaton.snowplow.kafka.serializer.ThriftSerializer;
import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;


public class ThriftSerdeTest {

  @Test
  public void testSerialize() {
	  CollectorPayload collectorPayload = new CollectorPayload();
	  collectorPayload.setCollector("dummy-collector");    
	  ThriftSerializer thriftSerializer = new ThriftSerializer();
	  byte[] data = thriftSerializer.serialize("snowplow-data", collectorPayload);
	  thriftSerializer.close();
	  
	  ThriftDeserializer thriftDeserializer = new ThriftDeserializer();
	  CollectorPayload anotherCollectorPayload = thriftDeserializer.deserialize("snowplow-data", data);
	  thriftDeserializer.close();
	  
      assertThat(collectorPayload).isEqualTo(anotherCollectorPayload);

  }
}
