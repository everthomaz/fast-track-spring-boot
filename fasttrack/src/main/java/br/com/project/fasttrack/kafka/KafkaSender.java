package br.com.project.fasttrack.kafka;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import br.com.project.fasttrack.controller.form.OrderForm;

@Service
public class KafkaSender {
	
	public void sendMessage(String value) throws InterruptedException, ExecutionException {
		var producer = new KafkaProducer<String, String>(properties());
		var key = UUID.randomUUID().toString();
		var record = new ProducerRecord<>("ORDERS", key + " - " + value, value);
		producer.send(record, (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println(data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/timestamp " + data.timestamp());
		}).get();
		
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "instance-oracle-2:29092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
