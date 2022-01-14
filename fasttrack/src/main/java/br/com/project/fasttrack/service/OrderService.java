package br.com.project.fasttrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.project.fasttrack.model.Order;
import br.com.project.fasttrack.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	public Iterable<Order> obterTodos() {

		Iterable<Order> orders = repository.findAll();

		return orders;

	}

	public void salvar(Order orders) {
		repository.save(orders);
	}
}
