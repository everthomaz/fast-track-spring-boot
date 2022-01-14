package br.com.project.fasttrack.consumer.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.project.fasttrack.consumer.model.Order;
import br.com.project.fasttrack.consumer.model.StatusOrder;
import br.com.project.fasttrack.consumer.repository.OrderRepository;

@Component
@Transactional
public class AtualizacaoOrderForm {
	
	@NotNull @NotEmpty
	private String description;
	@NotNull
	private Long id;
	@NotNull @NotEmpty
	private String name;
	@NotNull
	private Long total;
	@NotNull
	private StatusOrder status;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	public StatusOrder getStatus() {
		return status;
	}
	public void setStatus(StatusOrder status) {
		this.status = status;
	}
	
	public Order atualizar(Long id, OrderRepository orderRepository) {
		Order order = orderRepository.getOne(id);
		order.setDescription(order.getDescription());
		order.setName(order.getName());
		order.setTotal(order.getTotal());
		order.setStatus(status.PROCESSED);
		return order;
	}
}
