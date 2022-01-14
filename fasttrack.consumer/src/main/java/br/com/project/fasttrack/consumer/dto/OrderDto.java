package br.com.project.fasttrack.consumer.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.fasttrack.consumer.model.Order;
import br.com.project.fasttrack.consumer.model.StatusOrder;

public class OrderDto {
	
	private String description;
	private Long id;
	private String name;
	private Long total;
	private StatusOrder status;
	
	public OrderDto(Order order) {
		this.description = order.getDescription();
		this.id = order.getId();
		this.name = order.getName();
		this.total = order.getTotal();
		this.status = order.getStatus();
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

	public Long getTotal() {
		return total;
	}

	public StatusOrder getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}
	
	public static List<OrderDto> converter(List<Order> orders) {
		return orders.stream().map(OrderDto::new).collect(Collectors.toList());
	}

	public static OrderDto converter(Order order) {
		
		OrderDto orderDto = new OrderDto(order);
		return orderDto;
		
	}
	
	public String converterParaJson() {
		Order order = new Order();
        order.setDescription(this.description);
        order.setId(this.id);
        order.setName(this.name);
        order.setTotal(this.total);
        order.setStatus(this.status);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(order);
            System.out.println("ResultingJSONstring = " + json);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            }
		return null;
		
	}

}
