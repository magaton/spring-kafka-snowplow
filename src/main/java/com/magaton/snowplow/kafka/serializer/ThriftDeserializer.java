package com.magaton.snowplow.kafka.serializer;

import java.util.Arrays;
import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftDeserializer implements Deserializer<CollectorPayload> {

 
  @Override
  public void close() {
    // No-op
  }

  @Override
  public void configure(Map<String, ?> arg0, boolean arg1) {
    // No-op
  }

  @SuppressWarnings("unchecked")
  @Override
  public CollectorPayload deserialize(String topic, byte[] data) {
	  CollectorPayload cp = new CollectorPayload();
    try {
    	TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
    	deserializer.deserialize(cp, data);
     
      return cp;
    } catch (Exception ex) {
      throw new SerializationException(
          "Can't deserialize data '" + Arrays.toString(data) + "' from topic '" + topic + "'", ex);
    }
  }
}
