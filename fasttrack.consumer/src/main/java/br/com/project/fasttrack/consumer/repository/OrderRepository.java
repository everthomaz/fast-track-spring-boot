package br.com.project.fasttrack.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.project.fasttrack.consumer.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
