package com.magaton.snowplow.kafka.serializer;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

import com.snowplowanalytics.snowplow.CollectorPayload.thrift.model1.CollectorPayload;

public class ThriftSerializer implements Serializer<CollectorPayload> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// no-op
	}

	@Override
	public byte[] serialize(String topic, CollectorPayload collectorPayload) {
		try {
			TSerializer ser = new TSerializer(new TBinaryProtocol.Factory());
			return ser.serialize(collectorPayload);
		} catch (TException ex) {
			throw new SerializationException(
					"Can't serialize data='" + collectorPayload + "' for topic='" + topic + "'", ex);
		}
	}

	@Override
	public void close() {
		// no-op

	}

}
