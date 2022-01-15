package br.com.project.fasttrack.consumer.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.fasttrack.consumer.form.AtualizacaoOrderForm;
import br.com.project.fasttrack.consumer.model.Order;
import br.com.project.fasttrack.consumer.repository.OrderRepository;
import br.com.project.fasttrack.consumer.service.UpdateDatabase;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaService implements Closeable {
    
	private final KafkaConsumer<String, String> consumer;
    private final ConsumerFunction parse;
    
	public KafkaService(String groupId, String topic, ConsumerFunction parse) {
		this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(groupId));
        consumer.subscribe(Collections.singletonList(topic));
	}

	public void run(OrderRepository orderRepository, AtualizacaoOrderForm form) throws JsonMappingException, JsonProcessingException {
    	while(true) {
    		var records = consumer.poll(Duration.ofMillis(100));
    		if (!records.isEmpty()) {
    			System.out.println("Encontrei " + records.count() + " registros");
				
				for (var record : records) {
					parse.consume(record);
					ObjectMapper objectMapper = new ObjectMapper();
					Order order = objectMapper.readValue(record.value(), Order.class);
					UpdateDatabase updateDatabase = new UpdateDatabase();
					updateDatabase.update(order, orderRepository, form);
					System.out.println("Order processed");
				}
			}
		}
    }

	private static Properties properties(String groupId) {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "168.138.130.55:29092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		return properties;
	}

    @Override
    public void close() {
        consumer.close();
    }
}
