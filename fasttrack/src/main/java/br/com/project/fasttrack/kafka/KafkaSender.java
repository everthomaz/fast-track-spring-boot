package br.com.project.fasttrack.kafka;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender implements Closeable {
		
	private final KafkaProducer<String, String> producer;

	public KafkaSender() {
        this.producer = new KafkaProducer<>(properties());
    }
	
	private static Properties properties() {
		var properties = new Properties();
		//properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "168.138.130.55:29092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
	
	public void send(String topic, String key, String value) throws InterruptedException, ExecutionException {
		var record = new ProducerRecord<>(topic, key, value);
		producer.send(record, (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println(data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/timestamp " + data.timestamp());
		}).get();
		
	}
	
	@Override
    public void close() {
        producer.close();
    }
}
