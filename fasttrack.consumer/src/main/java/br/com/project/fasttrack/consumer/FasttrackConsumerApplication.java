package br.com.project.fasttrack.consumer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.project.fasttrack.consumer.form.AtualizacaoOrderForm;
import br.com.project.fasttrack.consumer.kafka.KafkaService;
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
		var consumer = new FasttrackConsumerApplication();
        try (var service = new KafkaService(FasttrackConsumerApplication.class.getSimpleName(),
                "ORDERS",
                consumer::parse)) {
            service.run(orderRepository, form);
        }
        
        
	}
	
	private void parse(ConsumerRecord<String, String> record) {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order.");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }	
    }
	
	
}
