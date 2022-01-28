package br.com.project.fasttrack.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.project.fasttrack.model.Order;
import br.com.project.fasttrack.model.StatusOrder;

public class OrderRequestDto {

	@NotNull @NotEmpty
	private String description;
	@NotNull @NotEmpty
	private Long id;
	@NotNull @NotEmpty
	private String name;
	@NotNull @NotEmpty
	private Long total;
	@NotNull @NotEmpty
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

	public Order converter() {
		return new Order(description, name, total, StatusOrder.NOT_PROCESSED);
	}

}
