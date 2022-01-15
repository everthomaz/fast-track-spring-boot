package br.com.project.fasttrack.consumer.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.project.fasttrack.consumer.form.AtualizacaoOrderForm;
import br.com.project.fasttrack.consumer.model.Order;
import br.com.project.fasttrack.consumer.repository.OrderRepository;

@Component
public class UpdateDatabase {
	
	public void update(Order order, OrderRepository orderRepository, AtualizacaoOrderForm form) throws JsonMappingException, JsonProcessingException {
		try {
			Optional<Order> optional = orderRepository.findById(order.getId());
			if (optional.isPresent()) {
				form.atualizar(optional.get().getId(), orderRepository);
			}
		} finally {
			System.out.println("Order updated.");
		}
	}
}
