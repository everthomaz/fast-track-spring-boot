package br.com.project.fasttrack.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.project.fasttrack.model.Order;
import br.com.project.fasttrack.model.StatusOrder;
import br.com.project.fasttrack.repository.OrderRepository;

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
		order.setDescription(this.description);
		order.setName(this.name);
		order.setTotal(this.total);
		order.setStatus(this.status);
		return order;
	}
}
