package br.com.project.fasttrack.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.fasttrack.consumer.form.AtualizacaoOrderForm;
import br.com.project.fasttrack.consumer.model.Order;
import br.com.project.fasttrack.consumer.repository.OrderRepository;

@SpringBootApplication
public class FasttrackConsumerApplication implements CommandLineRunner {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AtualizacaoOrderForm form;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FasttrackConsumerApplication.class, args);
	}
	
	public void run(String... args) throws Exception, InterruptedException, ExecutionException, JsonParseException, JsonParseException, JsonMappingException, JsonProcessingException{
		var consumer = new KafkaConsumer<String, String>(properties());
		consumer.subscribe(Collections.singletonList("ORDERS"));
		while(true) {
			var records = consumer.poll(Duration.ofMillis(10000));
			if (!records.isEmpty()) {
				System.out.println("Encontrei " + records.count() + " registros");
				
				for (var record : records) {
					System.out.println("----------------------------------------");
					System.out.println("Processing new order, checking for fraud");
					System.out.println(record.key());
					System.out.println(record.value());
					System.out.println(record.partition());
					System.out.println(record.offset());
					try {
						ObjectMapper objectMapper = new ObjectMapper();
						Order order = objectMapper.readValue(record.value(), Order.class);
						Long id = order.getId();
						Optional<Order> optional = orderRepository.findById(id);
						if (optional.isPresent()) {
							form.atualizar(optional.get().getId().longValue(), orderRepository);
							System.out.println("Order updated.");
						}
					} finally {
						System.out.println("Order processed");
					}
				}
			}
		}
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "instance-oracle-2:29092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FasttrackConsumerApplication.class.getName());
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, FasttrackConsumerApplication.class.getSimpleName() + "-" + UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		return properties;
	}
}
